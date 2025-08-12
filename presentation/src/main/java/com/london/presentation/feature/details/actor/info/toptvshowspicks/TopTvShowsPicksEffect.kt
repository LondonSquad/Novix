package com.london.presentation.feature.details.actor.info.toptvshowspicks

sealed interface TopTvShowsPicksEffect {
    data object NavigateBack : TopTvShowsPicksEffect
    data class NavigateToTvShowDetails(val tvShowId: Int) : TopTvShowsPicksEffect
}
