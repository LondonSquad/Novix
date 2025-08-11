package com.london.presentation.feature.details.actor.info.topmoviespicks

sealed interface TopMoviesPicksEffect {
    data object NavigateBack : TopMoviesPicksEffect
    data class NavigateToMovieDetails(val movieId: Int) : TopMoviesPicksEffect
}
