package com.london.presentation.screen.details.movieDetalis

interface MovieDetailsContract {
    fun onBackClick()
    fun onSavedClick()
    fun onExpandClick()
    fun onMovieClick(movieId: Int)
    fun onActorClick(actorId: Int)
    fun onReviewsClick(movieId: Int, mediaNumber: Int)
    fun onGenreClick(genreId: Int)
}