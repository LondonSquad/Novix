package com.london.presentation.feature.list.viewlistitems

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.movielist.GetMovieListDetailsUseCase
import com.london.domain.usecase.movielist.GetMovieListNameUseCase
import com.london.domain.usecase.movielist.ManageMovieListUseCase
import com.london.domain.usecase.movielist.RemoveMovieFromListUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewListItemsViewModel @Inject constructor(
    private val getMovieListDetailsUseCase: GetMovieListDetailsUseCase,
    private val removeMovieFromListUseCase: RemoveMovieFromListUseCase,
    private val getMovieListNameUseCase: GetMovieListNameUseCase,
    private val manageMovieListUseCase: ManageMovieListUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ViewListItemsUiState, ViewListItemsEffect>(ViewListItemsUiState()),
    ViewListItemsContract {

    private val args = savedStateHandle.getArgs<Screen.ViewListItems>()
    private val listId = args?.listId ?: 0

    init {
        getMovieListName(listId = listId)
        fetchMovieListDetails(listId = listId)
    }

    override fun onBack() {

        emitEffect(ViewListItemsEffect.NavigateBack)
    }

    override fun onRetry() {

        updateState { copy(error = null) }
        fetchMovieListDetails(listId)
    }

    override fun onDeleteClick() {

        updateState { copy(isDeleteBottomSheetVisible = true) }
    }

    override fun onConfirmDelete() {

        tryToExecute(
            block = {
                manageMovieListUseCase.deleteMovieList(listId.toUInt())
            },
            onCompleted = {
                updateState { copy(isDeleteBottomSheetVisible = false) }
            },
            onError = {
                updateState { copy(error = ErrorState.RequestFailed()) }
            },
            onSuccess = {
                emitEffect(ViewListItemsEffect.NavigateBack)
            }
        )
    }

    override fun onMovieClick(id: Int) {

        emitEffect(ViewListItemsEffect.NavigationMovieDetails(id))
    }

    override fun onRemoveMovieClick(id: Int) {

        tryToExecute(
            block = {
                removeMovieFromListUseCase.invoke(listId = listId.toUInt(), movieId = id.toUInt())
            },
            onStart = {
                updateState { copy(error = null, isSnackBarSuccessVisible = false) }
            },
            onError = {
                updateState { copy(error = ErrorState.EntryNotFound()) }
            },
            onSuccess = {
                updateState { copy(isSnackBarSuccessVisible = true) }
            }
        )
    }

    override fun onDeleteBottomSheetDismiss() {
        updateState { copy(isDeleteBottomSheetVisible = false) }
    }

    private fun fetchMovieListDetails(listId: Int) {

        tryToExecute(
            block = {
                val moviesFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val movies = getMovieListDetailsUseCase.invoke(
                        listId = listId.toUInt(),
                        pageNumber = pageNumber
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
                    copy(listItems = moviesFlow, listTitle = "marwan")
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

    private fun getMovieListName(listId: Int) {

        tryToExecute(
            block = {
                getMovieListNameUseCase.invoke(listId.toUInt())
            },
            onStart = {
                updateState { copy(isLoading = true) }
            },
            onSuccess = {
                updateState {
                    copy(listTitle = it)
                }
            }
        )
    }
}
