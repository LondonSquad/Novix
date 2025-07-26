package com.london.presentation.screen.home.trending.tvshow

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.london.domain.usecase.GetTrendingTvShowsUseCase
import com.london.presentation.screen.base.BaseViewModel
import com.london.presentation.screen.base.createPagingSourceFlow
import com.london.presentation.utils.TvShowGenre
import kotlinx.coroutines.launch

class TrendingTvShowsViewModel(
    private val getTrendingTvShows: GetTrendingTvShowsUseCase,
) : BaseViewModel<TrendingTvShowsUiState, TrendingTvShowsEffect>(TrendingTvShowsUiState()),
    TrendingTvShowsContract {

    init {
        fetchTrendingTvShows()
    }

    private fun fetchTrendingTvShows() {
        viewModelScope.launch {
            val tvShowsFlow = createPagingSourceFlow("") { _, pageNumber ->
                getTrendingTvShows.invoke(pageNumber)
            }.cachedIn(viewModelScope)
            updateState { copy(trendingTvShows = tvShowsFlow, isLoading = false) }
        }
    }

    override fun onGenreSelected(genre: TvShowGenre) {
        if (genre.id == state.value.selectedGenreId) return
        updateState { copy(selectedGenreId = genre.id) }
    }

    override fun onTvShowClick(id: Int) = emitEffect(TrendingTvShowsEffect.NavigateToTvShow(id))

    override fun onBackClick() = emitEffect(TrendingTvShowsEffect.NavigateBack)

} 