package com.london.presentation.screen.home.trending.tvshow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.london.domain.usecase.GetTrendingTvShowsUseCase
import com.london.presentation.screen.base.createPagingSourceFlow
import com.london.presentation.utils.TvShowGenre
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TrendingTvShowsViewModel(
    private val getTrendingTvShows: GetTrendingTvShowsUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TrendingTvShowsUiState())
    val state: StateFlow<TrendingTvShowsUiState> = _state

    private val _effect = MutableStateFlow<TrendingTvShowsEffect?>(null)
    val effect: StateFlow<TrendingTvShowsEffect?> = _effect.asStateFlow()

    init {
        _state.value = _state.value.copy(
            selectedGenreId = TvShowGenre.All.id
        )
        fetchTrendingTvShows()
    }

    private fun fetchTrendingTvShows() {
        val tvShowsFlow = createPagingSourceFlow("") { _, pageNumber ->
            getTrendingTvShows.invoke(pageNumber)
        }.cachedIn(viewModelScope)
        _state.value = _state.value.copy(trendingTvShows = tvShowsFlow, isLoading = false)
    }

    fun onGenreSelected(genre: TvShowGenre) {
        _state.value = _state.value.copy(selectedGenreId = genre.id)
    }

    fun onTvShowClick(tvShowId: Int) {
        _effect.value = TrendingTvShowsEffect.NavigateToTvShow(tvShowId)
    }

    fun onBackClick() {
        _effect.value = TrendingTvShowsEffect.NavigateBack
    }

    fun resetEffect() {
        _effect.value = null
    }
} 