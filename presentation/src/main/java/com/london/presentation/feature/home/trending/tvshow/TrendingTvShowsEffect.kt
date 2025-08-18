package com.london.presentation.feature.home.trending.tvshow

sealed interface TrendingTvShowsEffect {
    data object BackNavigation : TrendingTvShowsEffect
    data class TvShowDetailsNavigation(val tvShowId: Int) : TrendingTvShowsEffect
}
