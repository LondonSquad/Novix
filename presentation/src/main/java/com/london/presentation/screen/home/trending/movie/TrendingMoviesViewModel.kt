package com.london.presentation.screen.home.trending.movie

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.london.domain.usecase.GetTrendingMoviesUseCase
import com.london.presentation.screen.base.BaseViewModel
import com.london.presentation.screen.base.createPagingSourceFlow
import com.london.presentation.utils.MovieGenre
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TrendingMoviesViewModel(
    private val getTrendingMovies: GetTrendingMoviesUseCase,
) : BaseViewModel<TrendingMoviesUiState, TrendingMoviesEffect>(TrendingMoviesUiState()),
    TrendingMoviesContract{

    init {
        fetchTrendingMovies()
    }

    private fun fetchTrendingMovies() {
            val moviesFlow = createPagingSourceFlow("") { _, pageNumber ->
                getTrendingMovies.invoke(pageNumber)
            }.cachedIn(viewModelScope)
        updateState { copy(trendingMovies = moviesFlow, isLoading = false) }
    }

    override fun onGenreSelected(genre: MovieGenre) {
        if (genre.id == state.value.selectedGenreId) return
        updateState { copy(selectedGenreId = genre.id) }
    }

    override fun onMovieClick(id: Int) = emitEffect(TrendingMoviesEffect.NavigateToMovie(movieId))

    override fun onBackClick() = emitEffect(TrendingMoviesEffect.NavigateBack)
}
