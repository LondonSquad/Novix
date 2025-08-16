package com.london.presentation.feature.details.movie

import com.london.domain.entity.recent.MediaType
import com.london.presentation.shared.genre.MovieGenreUi

sealed interface MovieDetailsEffect {
    data object BackNavigation : MovieDetailsEffect
    data object OnLoginNavigation : MovieDetailsEffect
    data class MovieNavigation(val movieId: Int) : MovieDetailsEffect
    data class ActorNavigation(val actorId: Int) : MovieDetailsEffect
    data class GenreNavigation(val genre: MovieGenreUi) : MovieDetailsEffect
    data class ReviewsNavigation(val movieId: Int, val mediaType: MediaType) : MovieDetailsEffect
}