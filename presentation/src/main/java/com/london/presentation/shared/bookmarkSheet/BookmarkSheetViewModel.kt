package com.london.presentation.shared.bookmarkSheet

import com.london.domain.usecase.movielist.AddMovieToListUseCase
import com.london.domain.usecase.movielist.GetAllMovieListsUseCase
import com.london.domain.usecase.movielist.GetMovieListsUseCase
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkSheetViewModel @Inject constructor(
    private val addMovieToListUseCase: AddMovieToListUseCase,
    private val getMovieListsUseCase: GetMovieListsUseCase,
    private val getAllMovieListsUseCase: GetAllMovieListsUseCase
) : BaseViewModel<BookmarkSheetUiState, BookmarkSheetEffect>(BookmarkSheetUiState()),
    BookmarkSheetContract {

    override fun onListSelected(listId: UInt) = updateState {
        copy(selectedLists = selectedLists.toMutableList().apply { add(listId) })
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
            onError = { error ->
                updateState {
                    copy(
                        error = error,
                        isErrorSnackbarVisible = true
                    )
                }
            },
            onSuccess = {
                updateState {
                    copy(
                        isLoading = false,
                        isSuccessSnackbarVisible = true
                    )
                }
            }
        )
    }

    override fun onDismiss() = updateState {
        copy(
            selectedLists = emptyList(),
            isErrorSnackbarVisible = false,
            isSuccessSnackbarVisible = false
        )
    }

    override fun onCreateNewList() = emitEffect(BookmarkSheetEffect.NewListCreation)
}