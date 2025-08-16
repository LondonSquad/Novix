package com.london.presentation.feature.details.actor.info.topmoviespicks

sealed interface TopMoviesPicksEffect {
    data object BackNavigation : TopMoviesPicksEffect
    data class MovieDetailsNavigation(val movieId: Int) : TopMoviesPicksEffect
}
