package com.london.presentation.feature.details.actor

sealed interface ActorEffect {
    data object NavigateBack : ActorEffect
    data class NavigateToGallery(val actorId: Int) : ActorEffect
    data class NavigateToTopTvShowPicks(val actorId: Int) : ActorEffect
    data class NavigateToMovieScreen(val movieId: Int) : ActorEffect
    data class NavigateToTopMoviePicks(val actorId: Int) : ActorEffect
    data class NavigateToTvShowScreen(val tvShowId: Int) : ActorEffect
}
