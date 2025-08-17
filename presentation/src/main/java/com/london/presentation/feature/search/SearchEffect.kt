package com.london.presentation.feature.search

sealed interface SearchEffect {
    data class MovieNavigation(val movieId: Int) : SearchEffect
    data class TvShowNavigation(val tvId: Int) : SearchEffect
    data class ActorNavigation(val actorId: Int) : SearchEffect
}
