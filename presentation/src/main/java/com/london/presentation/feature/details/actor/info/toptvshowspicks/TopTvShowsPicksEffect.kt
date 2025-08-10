package com.london.presentation.feature.details.actor.info.toptvshowspicks

sealed interface TopTvShowsPicksEffect {
    data class TvShowNavigation(val tvShowId: Int) : TopTvShowsPicksEffect
    data object BackNavigation : TopTvShowsPicksEffect
}