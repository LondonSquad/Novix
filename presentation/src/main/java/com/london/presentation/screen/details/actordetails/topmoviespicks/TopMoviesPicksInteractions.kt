package com.london.presentation.screen.details.actordetails.topmoviespicks

interface TopMoviesPicksInteractions {
    fun onMovieClick(movieId: Int)
    fun onBackClick()
    fun onSaveMovie(movieId: Int)
}