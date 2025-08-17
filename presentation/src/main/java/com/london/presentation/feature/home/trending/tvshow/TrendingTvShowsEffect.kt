package com.london.presentation.feature.home.trending.tvshow

sealed interface TrendingTvShowsEffect {
    data object OnNavigateBackClick : TrendingTvShowsEffect
    data class OnNavigateToTvShowClick(val tvShowId: Int) : TrendingTvShowsEffect
}
