package com.london.presentation.feature.home.trending.tvshows

sealed interface TrendingTvShowsEffect {
    data class NavigateToTvShow(val tvShowId: Int) : TrendingTvShowsEffect
    data object NavigateBack : TrendingTvShowsEffect
}
