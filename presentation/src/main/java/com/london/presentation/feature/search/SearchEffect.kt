package com.london.presentation.feature.search

sealed interface SearchEffect {
    data class ToTvShowNavigation(val tvId: Int) : SearchEffect
    data class ToActorNavigation(val actorId: Int) : SearchEffect
    data class ToMovieNavigation(val movieId: Int) : SearchEffect
}