package com.london.presentation.feature.list.savedlist

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.london.domain.entity.movie.MovieList
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.movielist.ManageGetMovieUseCase
import com.london.domain.usecase.movielist.ManageMovieListUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class ListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageGetMovieUseCase: ManageGetMovieUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    private val manageMovieListUseCase: ManageMovieListUseCase
) : BaseViewModel<ListUiState, ListEffect>(ListUiState()), ListContract {

    private val args = savedStateHandle.getArgs<Screen.Lists>()

    init {
        checkUserLoginStatus { isLoggedIn ->
            if (!isLoggedIn) return@checkUserLoginStatus
            fetchSavedLists()
        }
        setAddListSheetVisible(args?.createList ?: false)
    }

    override fun onRetry() = fetchSavedLists()

    override fun onFabClick() = setAddListSheetVisible(true)

    override fun onLoginClick() = emitEffect(ListEffect.NavigateToLogin)

    override fun onListClick(id: Int) = emitEffect(ListEffect.NavigateToDetails(id))

    override fun setAddListSheetVisible(visible: Boolean) =
        updateState { copy(addListSheetState = addListSheetState.copy(isSheetVisible = visible)) }

    override fun onListNameChanged(listName: TextFieldValue) =
        updateState { copy(addListSheetState = addListSheetState.copy(listName = listName)) }

    override fun resetSnackBarErrorState() = updateState { copy(isSnackBarErrorVisible = false) }

    override fun resetSnackBarSuccessState() = updateState { copy(isSnackBarSuccessVisible = false) }

    override fun onAddList(listName: String) {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { manageMovieListUseCase.createMovieList(listName) },
            onError = { error -> handleAddListFail(error) },
            onSuccess = { handleAddListSuccess() }
        )
    }

    private fun handleAddListFail(error: ErrorState) {
        updateState {
            copy(
                error = error,
                isLoading = false,
                isSnackBarErrorVisible = true
            )
        }
    }

    private fun handleAddListSuccess() {
        updateState {
            copy(
                isSnackBarSuccessVisible = true,
                isLoading = false,
                addListSheetState = AddSheetState()
            )
        }
        fetchSavedLists()
    }

    private fun fetchSavedLists() {
        tryToExecute(
            block = { createListsPagingSource() },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { moviesFlow -> updateState { copy(items = moviesFlow) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun createListsPagingSource(): Flow<PagingData<MovieList>> =
        createPagingSourceFlow { _, pageNumber -> manageGetMovieUseCase.getAllMovieLists(pageNumber) }

    private fun checkUserLoginStatus(onResult: (Boolean) -> Unit = {}) {
        tryToExecute(
            block = { authenticationUseCase.isLoggedIn() },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { isLoggedIn -> handleUserLoginSuccess(isLoggedIn); onResult(isLoggedIn) },
            onError = { handleUserLoginFail(); onResult(false) },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun handleUserLoginSuccess(isLoggedIn: Boolean) =
        updateState { copy(isGuest = !isLoggedIn, isLoading = false) }

    private fun handleUserLoginFail() = updateState { copy(isGuest = true, isLoading = false) }

}
