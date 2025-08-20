package com.london.presentation.feature.search

sealed interface SearchEffect {
    data class TvShowDetailsNavigation(val tvId: Int) : SearchEffect
    data class MovieDetailsNavigation(val movieId: Int) : SearchEffect
    data class ActorDetailsNavigation(val actorId: Int) : SearchEffect
}
