package com.london.presentation.feature.details.movie

import com.london.domain.entity.recent.MediaType

sealed interface MovieDetailsEffect {
    data object BackNavigation : MovieDetailsEffect
    data object LoginNavigation : MovieDetailsEffect
    data class MovieNavigation(val movieId: Int) : MovieDetailsEffect
    data class ActorNavigation(val actorId: Int) : MovieDetailsEffect
    data class GenreNavigation(val genreId: Int) : MovieDetailsEffect
    data class ReviewsNavigation(val movieId: Int, val mediaType: MediaType) : MovieDetailsEffect
}