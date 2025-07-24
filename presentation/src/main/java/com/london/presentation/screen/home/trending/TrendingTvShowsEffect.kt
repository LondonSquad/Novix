package com.london.presentation.screen.home.trending

sealed class TrendingTvShowsEffect {
    data class NavigateToTvShow(val tvShowId: Int) : TrendingTvShowsEffect()
    data object NavigateBack : TrendingTvShowsEffect()
} 