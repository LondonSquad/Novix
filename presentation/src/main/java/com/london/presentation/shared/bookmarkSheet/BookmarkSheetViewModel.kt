package com.london.presentation.shared.bookmarkSheet

import com.london.domain.usecase.movielist.AddMovieToListUseCase
import com.london.domain.usecase.movielist.GetAllListedMovies
import com.london.domain.usecase.movielist.ManageMovieListUseCase
import com.london.domain.usecase.movielist.RemoveMovieFromListUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkSheetViewModel @Inject constructor(
    private val getAllListedMovies: GetAllListedMovies,
    private val addMovieToListUseCase: AddMovieToListUseCase,
    private val manageMovieListUseCase: ManageMovieListUseCase,
    private val removeMovieFromListUseCase: RemoveMovieFromListUseCase
) : BaseViewModel<BookmarkSheetUiState, BookmarkSheetEffect>(BookmarkSheetUiState()),
    BookmarkSheetContract {


    init {
        initializeListedMovies()
    }

    override fun onListSelected(listId: UInt) = updateState {
        copy(selectedLists = selectedLists.toMutableList().apply { add(listId) })
    }

    override fun onCreateNewList() {

    }

    override fun onAddToLists(bookmarkedId: UInt) {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = {
                state.value.selectedLists.forEach { listId ->
                    addMovieToListUseCase.invoke(
                        listId = listId,
                        movieId = bookmarkedId
                    )
                }
            },
            onError = { error -> updateState { copy(error = error) } },
            onSuccess = { updateState { copy(isLoading = false) } }
        )
    }

    override fun onDismiss() = updateState { copy(selectedLists = emptyList()) }


    private fun initializeListedMovies() {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { getAllListedMovies.invoke() },
            onError = { updateState { copy(error = ErrorState.EntryNotFound()) } },
            onSuccess = { updateState { copy(listedMovies = it) } }
        )
    }
}