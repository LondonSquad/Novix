package com.london.presentation.feature.category.moviesbycategory

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetMoviesByCategoryUseCase
import com.london.presentation.feature.base.BaseViewModel
import com.london.presentation.feature.base.createPagingSourceFlow
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoviesByCategoryViewModel @Inject constructor(
    private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<MoviesByCategoryUiState, MoviesByCategoryEffect>(MoviesByCategoryUiState()),
    MoviesByCategoryContract {

    private val args = savedStateHandle.getArgs<Screen.MoviesByCategory>()
    private val categoryId = args?.categoryId ?: 0

    init {
        initializeMovies(categoryId)
    }

    override fun onSavedClick(movieId: Int) {
        //toDo() save movie
    }

    override fun onMovieClick(movieId: Int) {
        emitEffect(MoviesByCategoryEffect.NavigateToMovieDetails(movieId = movieId))
    }

    override fun onBack() {
        emitEffect(MoviesByCategoryEffect.NavigateBack)
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
}
