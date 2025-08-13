package com.london.presentation.feature.category.movie

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.london.domain.entity.Movie
import com.london.domain.usecase.GetMoviesByCategoryUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieCategoryViewModel @Inject constructor(
    private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<MovieCategoryUiState, MovieCategoryEffect>(MovieCategoryUiState()),
    MovieCategoryContract {

    private val args = savedStateHandle.getArgs<Screen.MoviesByCategory>()
    private val categoryId = args?.categoryId ?: 0 //toDo() category id will replace with enum

    init {
        initializeMovies(categoryId)
    }

    override fun onMovieClick(movieId: Int) =
        emitEffect(MovieCategoryEffect.MovieDetailsNavigation(movieId = movieId))

    override fun onBack() =
        emitEffect(MovieCategoryEffect.NavigationBack)

    override fun onSavedClick(movieId: Int) = Unit //toDo() save movie

    private fun initializeMovies(categoryId: Int) {
        tryToExecute(
            onStart = { onInitializeMoviesStarted(categoryId = categoryId) },
            block = { createMoviesPagingSourceFlow(categoryId = categoryId) },
            onSuccess = ::onInitializeMoviesSuccess,
            checkSuccess = { categoryId != 0 },
            onError = ::onInitializeMoviesFailed,
            onCompleted = ::onInitializeMoviesCompleted
        )
    }

    private fun createMoviesPagingSourceFlow(categoryId: Int): Flow<PagingData<Movie>> {

        return createPagingSourceFlow(query = "") { _, pageNumber ->
            val movies = getMoviesByCategoryUseCase(
                categoryId = categoryId,
                pageNumber = pageNumber
            )
            movies.copy(items = movies.items)
        }
    }

    private fun onInitializeMoviesStarted(categoryId: Int) =
        updateState { copy(categoryId = categoryId, isLoading = true) }

    private fun onInitializeMoviesSuccess(moviesFlow: Flow<PagingData<Movie>>) =
        updateState { copy(moviesFlow = moviesFlow) }

    private fun onInitializeMoviesCompleted() =
        updateState { copy(isLoading = false) }

    private fun onInitializeMoviesFailed(errorState: ErrorState) =
        updateState { copy(error = errorState) }
}
