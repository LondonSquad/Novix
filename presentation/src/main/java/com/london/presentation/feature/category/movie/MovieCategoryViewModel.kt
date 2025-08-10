package com.london.presentation.feature.category.movie

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetMoviesByCategoryUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.createPagingSourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieCategoryViewModel @Inject constructor(
    private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<MovieCategoryUiState, MovieCategoryEffect>(MovieCategoryUiState()),
    MovieCategoryContract {

    private val args = savedStateHandle.getArgs<Screen.MoviesByCategory>()
    private val categoryId = args?.categoryId ?: 0

    init {
        initializeMovies(categoryId)
    }


    private fun initializeMovies(categoryId: Int) {
        tryToExecute(
            block = {
                val moviesFlow = createPagingSourceFlow(query = "") { _, pageNumber ->
                    val movies = getMoviesByCategoryUseCase(
                        categoryId = categoryId,
                        pageNumber = pageNumber
                    )
                    movies.copy(items = movies.items)
                }
                moviesFlow
            },
            onStart = {
                updateState { copy(categoryId = categoryId, isLoading = true) }
            },
            onSuccess = { moviesFlow ->
                updateState {
                    copy(movies = moviesFlow)
                }
            },
            onError = { errorState ->
                updateState {
                    copy(error = errorState)
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
            checkSuccess = { categoryId != 0 }
        )
    }

    override fun onSavedClick(movieId: Int) {
        //toDo() save movie
    }

    override fun onMovieClick(movieId: Int) {
        emitEffect(MovieCategoryEffect.NavigateToMovieDetails(movieId = movieId))
    }

    override fun onBack() {
        emitEffect(MovieCategoryEffect.NavigateBack)
    }
}
