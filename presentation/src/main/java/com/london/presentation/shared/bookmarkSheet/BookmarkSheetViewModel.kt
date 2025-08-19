package com.london.presentation.shared.bookmarkSheet

import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.movielist.GetAvailableListsForMovie
import com.london.domain.usecase.movielist.ManageMovieListUseCase
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarkSheetViewModel @Inject constructor(
    private val manageMovieListUseCase: ManageMovieListUseCase,
    private val getAvailableListsForMovie: GetAvailableListsForMovie,
    private val authenticationUseCase: AuthenticationUseCase
) : BaseViewModel<BookmarkSheetUiState, BookmarkSheetEffect>(BookmarkSheetUiState()),
    BookmarkSheetContract {

    init {
        initializeSessionStatus()
    }

    override fun onSheetShown(movieId: Int) {
        if (movieId == 0) return

        updateState {
            copy(
                selectedLists = emptyList(),
                isErrorSnackbarVisible = false,
                isSuccessSnackbarVisible = false,
                shouldDismiss = false,
                error = null,
                lists = emptyList()
            )
        }

        if (!state.value.isGuestSession) {
            initializeMovieLists(movieId)
        }
    }

    override fun onAddToLists(bookmarkedId: Int) {
        val listsToAdd = state.value.selectedLists.toList()

        tryToExecute(
            onStart = { updateState { copy(isAddingToList = true) } },
            block = {
                listsToAdd.forEach { listId ->
                    manageMovieListUseCase.addMovieToList(
                        listId = listId,
                        movieId = bookmarkedId
                    )
                }
            },
            onSuccess = {
                updateState {
                    copy(
                        isSuccessSnackbarVisible = true,
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
            },
            onCompleted = { updateState { copy(isAddingToList = false) } }
        )
    }

    override fun onDismiss() {
        updateState {
            copy(
                selectedLists = emptyList(),
                isErrorSnackbarVisible = false,
                isSuccessSnackbarVisible = false,
                shouldDismiss = false,
                error = null,
            )
        }
    }

    override fun onListSelected(listId: Int) = updateState {
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

    private fun initializeMovieLists(movieId: Int) {
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
