package com.london.presentation.feature.details.actordetails.topmoviespicks

sealed interface TopMoviesPicksEffect {
    data object NavigateBack : TopMoviesPicksEffect
    data class NavigationToMovieDetails(val movieId: Int) : TopMoviesPicksEffect
}