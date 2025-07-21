package com.london.presentation.screen.details.actordetails.toptvshowspicks

sealed interface TopTvShowsPicksEffect {
    data class TvShowNavigation(val tvShowId: Int) : TopTvShowsPicksEffect
    data object BackNavigation : TopTvShowsPicksEffect
}