package com.london.presentation.shared.bookmarkSheet

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.movielist.AddMovieToListUseCase
import com.london.domain.usecase.movielist.GetAllListedMovies
import com.london.domain.usecase.movielist.GetAllMovieListsUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkSheetViewModel @Inject constructor(
    private val addMovieToListUseCase: AddMovieToListUseCase,
    private val getAllMovieListsUseCase: GetAllMovieListsUseCase,
    private val getAllListedMovies: GetAllListedMovies,
    private val authenticationUseCase: AuthenticationUseCase
) : BaseViewModel<BookmarkSheetUiState, BookmarkSheetEffect>(BookmarkSheetUiState()),
    BookmarkSheetContract {

    init {
        initializeMovieLists()
        initializeSessionStatus()
    }

    override fun onSheetShown(movieId: UInt) {
        // Don't run this for guest users or if the movie ID is invalid
        if (state.value.isGuestSession || movieId == 0u) return

        tryToExecute(
            block = { getAllListedMovies.invoke() },
            onSuccess = { movieToListsMap ->
                val preSelectedLists =
                    movieToListsMap.getOrDefault(movieId, emptySet())
                        .toList()
                updateState { copy(selectedLists = preSelectedLists) }
            },
            onError = { updateState { copy(listError = it) } }
        )
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
            onSuccess = {
                updateState {
                    copy(
                        isLoading = false,
                        isSuccessSnackbarVisible = true,
                        selectedLists = emptyList(),
                    )
                }
                emitEffect(BookmarkSheetEffect.ItemSuccessfulAddition)
            },
            onError = { error ->
                updateState {
                    copy(
                        error = error,
                        isErrorSnackbarVisible = true
                    )
                }
            }
        )
    }

    override fun onDismiss() = updateState {
        copy(
            selectedLists = emptyList(),
            isErrorSnackbarVisible = false,
            isSuccessSnackbarVisible = false,
            error = null,
            listError = null
        )
    }

    override fun onListSelected(listId: UInt) = updateState {
        val currentSelectedLists = selectedLists.toMutableList()
        if (listId in currentSelectedLists) {
            currentSelectedLists.remove(listId)
        } else {
            currentSelectedLists.add(listId)
        }
        copy(selectedLists = currentSelectedLists)
    }

    override fun onCreateNewList() = emitEffect(BookmarkSheetEffect.NewListCreation)

    override fun onSnackbarShown() {
        updateState {
            copy(
                isSuccessSnackbarVisible = false,
                isErrorSnackbarVisible = false,
                error = null
            )
        }
    }

    override fun onLoginClick() {
        emitEffect(BookmarkSheetEffect.LoginNavigation)
    }

    private fun initializeMovieLists() {
        tryToExecute(
            block = {
                createPagingSourceFlow(query = "") { _, pageNumber ->
                    getAllMovieListsUseCase.invoke(pageNumber)
                }.cachedIn(viewModelScope)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { lists -> updateState { copy(lists = lists.toBookmarkUiLists()) } },
            onError = { error -> updateState { copy(error = error) } },
            onCompleted = { updateState { copy(isLoading = false) } }
        )
    }

    private fun initializeSessionStatus() {
        tryToExecute(
            block = { authenticationUseCase.isLoggedIn() },
            onSuccess = { isLoggedIn -> updateState { copy(isGuestSession = isLoggedIn.not()) } },
            onError = { errorState -> updateState { copy(error = errorState) } }
        )
    }

}
