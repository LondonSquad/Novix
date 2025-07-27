package com.london.presentation.screen.home.trending.movie

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.london.domain.usecase.GetTrendingMoviesUseCase
import com.london.presentation.screen.base.BaseViewModel
import com.london.presentation.screen.base.createPagingSourceFlow
import com.london.presentation.utils.MovieGenre
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TrendingMoviesViewModel(
    private val getTrendingMovies: GetTrendingMoviesUseCase,
) : BaseViewModel<TrendingMoviesUiState, TrendingMoviesEffect>(TrendingMoviesUiState()),
    TrendingMoviesContract {

    init {
        fetchTrendingMovies()
    }

    private fun fetchTrendingMovies(genreId: Int? = null) {
        val moviesFlow = createPagingSourceFlow("") { _, pageNumber ->
            getTrendingMovies.invoke(pageNumber, genreId)
        }.cachedIn(viewModelScope)
        updateState { copy(moviesFlow = moviesFlow, isLoading = false) }
    }

    override fun onGenreSelected(genre: MovieGenre) {
        if (genre.id == state.value.selectedGenreId) return
        updateState { copy(selectedGenreId = genre.id) }
        fetchTrendingMovies(genre.id)
    }

    override fun onMovieClick(id: Int) = emitEffect(TrendingMoviesEffect.NavigateToMovie(id))

    override fun onBackClick() = emitEffect(TrendingMoviesEffect.NavigateBack)
}
