package com.london.presentation.screen.category.moviesbycategory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.london.domain.entity.Movie
import com.london.domain.usecase.GetMoviesByCategoryUseCase
import com.london.presentation.navigation.arguments.MoviesByCategoryArgs
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
    @Provided
    private val getMoviesByCategoryUseCase: GetMoviesByCategoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), MoviesByCategoryInteractions {
    private val _uiState = MutableStateFlow(MoviesByCategoryUiState())
    val uiState: StateFlow<MoviesByCategoryUiState> = _uiState.asStateFlow()

    init {
        val args by lazy { MoviesByCategoryArgs(savedStateHandle) }
        viewModelScope.launch {
            initializeMovies(args.categoryId)
        }
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

    override fun onSavedClick(movieId: Int) {
        //save movie
    }
}