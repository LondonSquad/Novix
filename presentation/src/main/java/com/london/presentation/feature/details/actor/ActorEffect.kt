package com.london.presentation.feature.details.actor

sealed interface ActorEffect {
    data object BackNavigation : ActorEffect
    data class GalleryNavigation(val actorId: Int) : ActorEffect
    data class TopTvShowPicksNavigation(val actorId: Int) : ActorEffect
    data class MovieScreenNavigation(val movieId: Int) : ActorEffect
    data class TopMoviePicksNavigation(val actorId: Int) : ActorEffect
    data class TvShowScreenNavigation(val tvShowId: Int) : ActorEffect
}
