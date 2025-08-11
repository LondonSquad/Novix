package com.london.presentation.feature.category.movie

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.details.movie.GetMovieUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieCategoryViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<MovieCategoryUiState, MovieCategoryEffect>(MovieCategoryUiState()),
    MovieCategoryContract {

    private val args = savedStateHandle.getArgs<Screen.MoviesByCategory>()
    private val categoryId = args?.categoryId ?: 0

    init {
        initializeMovies(categoryId)
    }

    override fun onMovieClick(movieId: Int) =
        emitEffect(MovieCategoryEffect.NavigateToMovieDetails(movieId = movieId))

    override fun onBack() =
        emitEffect(MovieCategoryEffect.NavigateBack)

    override fun onSavedClick(movieId: Int) = Unit //toDo() save movie

    private fun initializeMovies(categoryId: Int) {
        tryToExecute(
            block = {
                createPagingSourceFlow(query = "") { _, pageNumber ->
                    val movies = getMovieUseCase.getMoviesByCategory(
                        categoryId = categoryId,
                        pageNumber = pageNumber
                    )
                    movies.copy(items = movies.items)
                }
            },
            onStart = {
                updateState { copy(categoryId = categoryId, isLoading = true) }
            },
            onSuccess = { moviesFlow ->
                updateState { copy(movies = moviesFlow) }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { categoryId != 0 }
        )
    }
}
