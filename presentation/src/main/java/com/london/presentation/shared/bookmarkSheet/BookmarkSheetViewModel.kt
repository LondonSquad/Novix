package com.london.presentation.shared.bookmarkSheet

import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.movielist.AddMovieToListUseCase
import com.london.domain.usecase.movielist.GetAvailableListsForMovie
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkSheetViewModel @Inject constructor(
    private val addMovieToListUseCase: AddMovieToListUseCase,
    private val getAvailableListsForMovie: GetAvailableListsForMovie,
    private val authenticationUseCase: AuthenticationUseCase
) : BaseViewModel<BookmarkSheetUiState, BookmarkSheetEffect>(BookmarkSheetUiState()),
    BookmarkSheetContract {

    init {
        initializeSessionStatus()
    }

    override fun onSheetShown(movieId: UInt) {
        if (state.value.isGuestSession || movieId == 0u) return
        initializeMovieLists(movieId)
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
                        lists = lists.filterNot { it.id in state.value.selectedLists },
                        selectedLists = emptyList(),
                        shouldDismiss = true
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
            }
        )
    }

    override fun onDismiss() = updateState {
        copy(
            selectedLists = emptyList(),
            isErrorSnackbarVisible = false,
            isSuccessSnackbarVisible = false,
            shouldDismiss = false,
            error = null,
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

    private fun initializeMovieLists(movieId: UInt) {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { getAvailableListsForMovie.invoke(movieId = movieId) },
            onSuccess = { lists -> updateState { copy(lists = lists.toBookmarkUiLists()) } },
            onCompleted = { updateState { copy(isLoading = false) } },
            onError = { error -> updateState { copy(error = error) } },
        )
    }

    private fun initializeSessionStatus() {
        tryToExecute(
            block = { authenticationUseCase.isLoggedIn() },
            onSuccess = { isLoggedIn -> updateState { copy(isGuestSession = isLoggedIn.not()) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
        )
    }
}
