package com.london.presentation.feature.details.actor.info.topmoviespicks

interface TopMoviesPicksContract {
    fun onRetry()
    fun onBackClick()
    fun onSaveClick(movieId: Int)
    fun onMovieClick(movieId: Int)
}
