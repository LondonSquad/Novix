package com.london.presentation.feature.search

sealed interface SearchEffect {
    data class NavigationTvShowDetails(val tvId: Int) : SearchEffect
    data class NavigationActorDetails(val actorId: Int) : SearchEffect
    data class NavigationMovieDetails(val movieId: Int) : SearchEffect
}