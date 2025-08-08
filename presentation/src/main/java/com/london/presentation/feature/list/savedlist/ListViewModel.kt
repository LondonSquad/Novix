package com.london.presentation.feature.list.savedlist

import androidx.compose.ui.text.input.TextFieldValue
import com.london.domain.usecase.login.AuthenticationUseCase
import com.london.domain.usecase.movielist.GetAllMovieListsUseCase
import com.london.domain.usecase.movielist.ManageMovieListUseCase
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllMovieListsUseCase: GetAllMovieListsUseCase,
    private val manageMovieListUseCase: ManageMovieListUseCase,
    private val authenticationUseCase: AuthenticationUseCase
) : BaseViewModel<ListUiState, ListEffect>(ListUiState()), ListContract {

    init {

        checkUserLoginStatus { isLoggedIn->
            if (!isLoggedIn) return@checkUserLoginStatus
            fetchSavedLists()
        }
    }

    override fun onRetry() { fetchSavedLists() }

    override fun onFabClick() = setAddListSheetVisible(true)


    override fun onLoginClick() { emitEffect(ListEffect.NavigateToLogin) }

    override fun onListClick(id: Int) {

        updateState { copy(isSnackBarSuccessVisible = false) }
        emitEffect(ListEffect.NavigateToDetails(id))
    }

    override fun setAddListSheetVisible(visible: Boolean) {

        updateState {
            copy(addListSheetState = addListSheetState.copy(isSheetVisible = visible))
        }
    }
    override fun onListNameChanged(listName: TextFieldValue) {

        updateState {
            copy(addListSheetState = addListSheetState.copy(listName = listName))
        }
    }

    override fun onAddList(listName: String) {

        tryToExecute(
            onStart = {
                updateState { copy(isSnackBarSuccessVisible = false,isLoading = true) }
            },
            block = {
                manageMovieListUseCase.createMovieList(listName)
            },
            onError = {
                updateState { copy(error = it, isLoading = false) }
            },
            onSuccess = {
                setAddListSheetVisible(false)
                updateState {
                    copy(
                        isSnackBarSuccessVisible = true,
                        isLoading = false,
                        addListSheetState = addListSheetState.copy(
                            listName = TextFieldValue(""),
                        )
                    )
                }
                fetchSavedLists()
            }
        )
    }

    private fun fetchSavedLists() {

        tryToExecute(
            block = {
                val moviesFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val movies = getAllMovieListsUseCase.invoke(
                        pageNumber
                    )
                    movies.copy(items = movies.items)
                }
                moviesFlow
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { moviesFlow ->
                updateState {
                    copy(items = moviesFlow)
                }
            },
            onError = { errorState ->
                updateState {
                    copy(error = errorState)
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun checkUserLoginStatus(onResult: (Boolean) -> Unit = {}) {

        tryToExecute(
            block = { authenticationUseCase.isLoggedIn() },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = { isLoggedIn ->
                updateState { copy(isGuest = !isLoggedIn, isLoading = false) }
                onResult(isLoggedIn)
            },
            onError = {
                updateState { copy(isGuest = true, isLoading = false) }
                onResult(false)
            },
        )
    }
}
