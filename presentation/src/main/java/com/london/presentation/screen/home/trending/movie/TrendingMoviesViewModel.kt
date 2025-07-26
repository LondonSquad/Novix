package com.london.presentation.screen.home.trending.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.london.domain.usecase.GetTrendingMoviesUseCase
import com.london.presentation.screen.base.createPagingSourceFlow
import com.london.presentation.utils.MovieGenre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TrendingMoviesViewModel(
    private val getTrendingMovies: GetTrendingMoviesUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TrendingMoviesUiState())
    val state: StateFlow<TrendingMoviesUiState> = _state

    private val _effect = MutableStateFlow<TrendingMoviesEffect?>(null)
    val effect: StateFlow<TrendingMoviesEffect?> = _effect.asStateFlow()

    init {
        _state.value = _state.value.copy(
            selectedGenreId = MovieGenre.All.id
        )
        fetchTrendingMovies()
    }

    private fun fetchTrendingMovies() {
        val moviesFlow = createPagingSourceFlow("") { _, pageNumber ->
            getTrendingMovies.invoke(pageNumber)
        }.cachedIn(viewModelScope)
        _state.value = _state.value.copy(trendingMovies = moviesFlow, isLoading = false)
    }

    fun onGenreSelected(genre: MovieGenre) {
        _state.value = _state.value.copy(selectedGenreId = genre.id)
    }

    fun onMovieClick(movieId: Int) {
        _effect.value = TrendingMoviesEffect.NavigateToMovie(movieId)
    }

    fun onBackClick() {
        _effect.value = TrendingMoviesEffect.NavigateBack
    }

    fun resetEffect() {
        _effect.value = null
    }
}
