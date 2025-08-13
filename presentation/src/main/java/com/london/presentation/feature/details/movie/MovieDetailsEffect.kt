package com.london.presentation.feature.details.movie

sealed interface MovieDetailsEffect {
    data object NavigateBack : MovieDetailsEffect
    data object NavigateToLogin : MovieDetailsEffect
    data class NavigateToMovie(val movieId: Int) : MovieDetailsEffect
    data class NavigateToActor(val actorId: Int) : MovieDetailsEffect
    data class NavigateToGenreMovies(val genreId: Int) : MovieDetailsEffect
    data class NavigateToReviews(val movieId: Int, val mediaNumber: Int) : MovieDetailsEffect
}