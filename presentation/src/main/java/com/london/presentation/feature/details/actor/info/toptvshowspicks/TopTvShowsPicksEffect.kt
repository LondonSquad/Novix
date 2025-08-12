package com.london.presentation.feature.details.actor.info.toptvshowspicks

sealed interface TopTvShowsPicksEffect {
    data object BackNavigation : TopTvShowsPicksEffect
    data class TvShowDetailsNavigation(val tvShowId: Int) : TopTvShowsPicksEffect
}
