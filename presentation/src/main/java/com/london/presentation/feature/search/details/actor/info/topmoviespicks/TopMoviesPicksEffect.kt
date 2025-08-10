package com.london.presentation.feature.search.details.actor.info.topmoviespicks

sealed interface TopMoviesPicksEffect {
    data object NavigateBack : TopMoviesPicksEffect
    data class NavigationToMovieDetails(val movieId: Int) : TopMoviesPicksEffect
}