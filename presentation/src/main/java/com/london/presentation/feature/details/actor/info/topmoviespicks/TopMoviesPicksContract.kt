package com.london.presentation.feature.details.actor.info.topmoviespicks

interface TopMoviesPicksContract {
    fun onBackClick()
    fun onRetryClick()
    fun onMovieClick(movieId: Int)
    fun onSaveMovieClick(movieId: Int)
}
