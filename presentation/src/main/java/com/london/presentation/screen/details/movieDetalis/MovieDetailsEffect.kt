package com.london.presentation.screen.details.movieDetalis

sealed interface MovieDetailsEffect {
    data object BackNavigation : MovieDetailsEffect
    data class MovieNavigation(val movieId: Int) : MovieDetailsEffect
    data class ActorNavigation(val actorId: Int) : MovieDetailsEffect
    data class ReviewsNavigation(val movieId: Int, val mediaNumber: Int) : MovieDetailsEffect
    data class GenreNavigation(val genreId: Int) : MovieDetailsEffect
}