package com.london.presentation.screen.category.moviesbycategory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.london.domain.entity.Movie
import com.london.domain.usecase.GetMoviesByCategoryUseCase
import com.london.presentation.navigation.arguments.MoviesByCategoryArgs
import com.london.presentation.screen.base.createPagingSourceFlow
import com.london.presentation.utils.launchCatching
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

@KoinViewModel
class MoviesByCategoryViewModel(
    @Provided private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), MoviesByCategoryInteractions {
    private val _uiState = MutableStateFlow(MoviesByCategoryUiState())
    val uiState: StateFlow<MoviesByCategoryUiState> = _uiState.asStateFlow()
    val args by lazy { MoviesByCategoryArgs(savedStateHandle) }

    init {
        launchCatching {
            initializeMovies(args.categoryId)
        }
    }


    private fun initializeMovies(categoryId: Int) {
        runCatching {
            _uiState.update {
                it.copy(categoryId = categoryId)
            }
            val moviesFlow = createPagingSourceFlow<Movie>(query = "") { currentQuery, pageNumber ->
                val movies = getMoviesByCategoryUseCase(
                    categoryId = categoryId,
                    language = "en-US",
                    pageNumber = pageNumber
                )
                movies.copy(items = movies.items)
            }
            _uiState.update {
                it.copy(
                    movies = moviesFlow,
                )
            }
        }.onFailure { e ->
            _uiState.update {
                it.copy(
                    error = e.message,
                )
            }
        }

    }

    override fun onSavedClick(movieId: Int) {
        //toDo() save movie
    }
}