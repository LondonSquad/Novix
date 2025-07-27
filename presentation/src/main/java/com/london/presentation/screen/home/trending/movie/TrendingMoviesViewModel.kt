package com.london.presentation.screen.home.trending.movie

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.usecase.GetTrendingMoviesUseCase
import com.london.presentation.screen.base.BaseViewModel
import com.london.presentation.screen.base.createPagingSourceFlow
import com.london.presentation.utils.MovieGenre
import org.koin.android.annotation.KoinViewModel
import org.koin.core.qualifier.qualifier

@KoinViewModel
class TrendingMoviesViewModel(
    private val getTrendingMovies: GetTrendingMoviesUseCase,
) : BaseViewModel<TrendingMoviesUiState, TrendingMoviesEffect>(TrendingMoviesUiState()),
    TrendingMoviesContract {

    init {
        fetchTrendingMovies()
    }

    private fun fetchTrendingMovies() {
        val moviesFlow = createPagingSourceFlow("") { _, pageNumber ->
            val response = getTrendingMovies.invoke(pageNumber)
            val filteredItems = response.items
                .filter {
                it.genreIds.contains(state.value.selectedGenreId)
            }
            PagedFetchResponse(
                currentPage = response.currentPage,
                items = filteredItems,
                totalPages = response.totalPages,
                totalItems = response.totalItems
            )
        }.cachedIn(viewModelScope)
        updateState { copy(moviesFlow = moviesFlow, isLoading = false) }
    }

    override fun onGenreSelected(genre: MovieGenre) {
        if (genre.id == state.value.selectedGenreId) return
        updateState {
            copy(selectedGenreId = genre.id)
        }
        fetchTrendingMovies()
    }

    override fun onMovieClick(id: Int) = emitEffect(TrendingMoviesEffect.NavigateToMovie(id))

    override fun onBackClick() = emitEffect(TrendingMoviesEffect.NavigateBack)
}
