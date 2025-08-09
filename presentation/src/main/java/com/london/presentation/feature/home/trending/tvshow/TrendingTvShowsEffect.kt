package com.london.presentation.feature.home.trending.tvshow

sealed interface TrendingTvShowsEffect {
    data object NavigateBack : TrendingTvShowsEffect
    data class NavigateToTvShow(val tvShowId: Int) : TrendingTvShowsEffect
}
