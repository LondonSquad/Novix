package com.london.presentation.screen.category.moviesbycategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.entity.Movie
import com.london.domain.usecase.GetMoviesByCategoryUseCase
import com.london.presentation.screen.base.createPagingSourceFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

@KoinViewModel
class MoviesByCategoryViewModel(
    @Provided private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase
) : ViewModel(), MoviesByCategoryInteractions {
    private val _uiState = MutableStateFlow(MoviesByCategoryUiState())
    val uiState: StateFlow<MoviesByCategoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            initializeMovies(27)
        }
    }

    override fun onMovieClick(movieId: Int) {
        //navigate to movie details
    }

    private fun initializeMovies(categoryId: Int) {
        _uiState.update {
            it.copy(categoryId = categoryId)
        }
        val moviesFlow = createPagingSourceFlow<Movie>(query = "") { currentQuery, pageNumber ->
            val movies = getMoviesByCategoryUseCase(
                categoryId = categoryId, language = "en-US", pageNumber = pageNumber
            )
            movies.copy(items = movies.items)
        }
        _uiState.update {
            it.copy(
                movies = moviesFlow,
            )
        }
    }

    override fun onBackClick() {
        //navigate back
    }

    override fun onSavedClick(movieId: Int) {
        //save movie
    }
}