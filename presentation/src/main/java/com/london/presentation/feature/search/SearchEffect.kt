package com.london.presentation.feature.search

sealed interface SearchEffect {
    data class ActorNavigation(val actorId: Int) : SearchEffect
    data class MovieNavigation(val movieId: Int) : SearchEffect
    data class TvNavigation(val tvId: Int) : SearchEffect
}