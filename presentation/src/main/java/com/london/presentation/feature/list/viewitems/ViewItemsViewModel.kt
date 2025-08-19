package com.london.presentation.feature.list.viewitems

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.london.domain.entity.movie.Movie
import com.london.domain.usecase.movielist.GetMovieListNameUseCase
import com.london.domain.usecase.movielist.ManageGetMovieUseCase
import com.london.domain.usecase.movielist.ManageMovieListUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ViewItemsViewModel @Inject constructor(
    private val manageGetMovieUseCase: ManageGetMovieUseCase,
    private val getMovieListNameUseCase: GetMovieListNameUseCase,
    private val manageMovieListUseCase: ManageMovieListUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ViewItemsUiState, ViewItemsEffect>(ViewItemsUiState()),
    ViewItemsContract {

    private val args = savedStateHandle.getArgs<Screen.ViewListItems>()
    private val listId = args?.listId ?: 0

    init {
        getMovieListName(listId = listId)
        fetchMovieListDetails(listId = listId)
    }

    override fun onBackClick() = emitEffect(ViewItemsEffect.NavigateBack)

    override fun onRetryClick() {
        updateState { copy(error = null) }
        fetchMovieListDetails(listId)
    }

    override fun onDeleteClick() = updateState { copy(isDeleteBottomSheetVisible = true) }

    override fun onConfirmDeleteClick() {
        tryToExecute(
            block = { manageMovieListUseCase.deleteMovieList(listId) },
            onCompleted = { updateState { copy(isDeleteBottomSheetVisible = false) } },
            onError = { updateState { copy(error = ErrorState.RequestFailed()) } },
            onSuccess = { emitEffect(ViewItemsEffect.NavigateBack) }
        )
    }

    override fun onMovieClick(id: Int) = emitEffect(ViewItemsEffect.NavigationMovieDetails(id))

    override fun onRemoveMovieClick(id: Int) {
        tryToExecute(
            block = { manageMovieListUseCase.removeMovieFromList(listId = listId, movieId = id) },
            onStart = { updateState { copy(error = null, isSnackBarSuccessVisible = false) } },
            onError = { updateState { copy(error = ErrorState.RequestFailed()) } },
            onSuccess = { updateState { copy(isSnackBarSuccessVisible = true) } }
        )
    }

    override fun onDeleteBottomSheetDismissClick() {
        updateState { copy(isDeleteBottomSheetVisible = false) }
    }

    private fun fetchMovieListDetails(listId: Int) {
        tryToExecute(
            block = { createMoviesPagingFlow(listId) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { moviesFlow -> updateState { copy(listItems = moviesFlow) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } }
        )
    }

    private fun getMovieListName(listId: Int) {
        tryToExecute(
            block = { getMovieListNameUseCase.invoke(listId) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = {
                updateState { copy(listTitle = it) }
            }
        )
    }

    private fun createMoviesPagingFlow(listId: Int): Flow<PagingData<Movie>> =
        createPagingSourceFlow(query = "") { _, pageNumber ->
            val movies = manageGetMovieUseCase.getMovieListDetails(
                listId = listId,
                pageNumber = pageNumber
            )
            movies.copy(items = movies.items)
        }
}
