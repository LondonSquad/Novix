package com.london.presentation.feature.list.details

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.london.domain.entity.movie.Movie
import com.london.domain.usecase.movielist.GetMovieListNameUseCase
import com.london.domain.usecase.movielist.ManageGetMovieUseCase
import com.london.domain.usecase.movielist.ManageMovieListUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieListDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageGetMovieUseCase: ManageGetMovieUseCase,
    private val manageMovieListUseCase: ManageMovieListUseCase,
    private val getMovieListNameUseCase: GetMovieListNameUseCase
) : BaseViewModel<MovieListUiState, MovieListEffect>(MovieListUiState()),
    MovieListDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.ViewListItems>()
    private val listId = args?.listId ?: 0

    init {
        getMovieItemsInfo()
    }

    private fun getMovieItemsInfo() {
        getMovieListName(listId = listId)
        fetchMovieListDetails(listId = listId)
    }

    override fun onBackClick() {
        emitEffect(MovieListEffect.NavigateBack)
    }

    override fun onRetryClick() {
        updateState { copy(error = null) }
        fetchMovieListDetails(listId)
    }

    override fun onDeleteClick() = updateState { copy(isDeleteBottomSheetVisible = true) }


    override fun onConfirmDelete() {
        tryToExecute(
            onStart = { resetSnackBarsState() },
            block = { manageMovieListUseCase.deleteMovieList(listId) },
            onCompleted = { updateState { copy(isDeleteBottomSheetVisible = false) } },
            onError = { updateState { copy(isSnackBarErrorVisible = true, error = it) } },
            onSuccess = {
                emitEffect(MovieListEffect.NavigateBack)
                updateState { copy(isListSnackBarSuccess = true) }
            }
        )
    }

    override fun onMovieClick(id: Int) {
        resetSnackBarsState()
        emitEffect(MovieListEffect.NavigationMovieDetails(id))
    }

    override fun onRemoveMovieClick(id: Int) {
        tryToExecute(
            block = { manageMovieListUseCase.removeMovieFromList(listId = listId, movieId = id) },
            onStart = { resetSnackBarsState() },
            onError = { updateState { copy(isSnackBarErrorVisible = true, error = it) } },
            onSuccess = { updateState { copy(isMovieSnackBarSuccessVisible = true) } }
        )
    }

    override fun onDeleteBottomSheetDismiss() {
        updateState { copy(isDeleteBottomSheetVisible = false) }
    }

    private fun resetSnackBarsState() {
        updateState {
            copy(
                error = null,
                isMovieSnackBarSuccessVisible = false,
                isSnackBarErrorVisible = false,
                isListSnackBarSuccess = false
            )
        }
    }

    override fun resetMovieSnackBarSuccessState() =
        updateState { copy(isMovieSnackBarSuccessVisible = false) }

    override fun resetSnackBarErrorState() = updateState { copy(isSnackBarErrorVisible = false) }

    override fun resetListSnackBarSuccessState() = updateState { copy(isListSnackBarSuccess = false) }

    private fun fetchMovieListDetails(listId: Int) {
        tryToExecute(
            block = { createMoviesPagingSource(listId = listId) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { moviesFlow -> updateState { copy(listItems = moviesFlow) } },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun createMoviesPagingSource(listId: Int): Flow<PagingData<Movie>> {
        return createPagingSourceFlow { _, pageNumber ->
            manageGetMovieUseCase.getMovieListDetails(
                listId = listId,
                pageNumber = pageNumber
            )
        }
    }

    private fun getMovieListName(listId: Int) {
        tryToExecute(
            block = { getMovieListNameUseCase.invoke(listId) },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { updateState { copy(listTitle = it) } }
        )
    }
}
