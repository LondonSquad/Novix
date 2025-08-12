package com.london.presentation.feature.details.actor.info.topmoviespicks

interface TopMoviesPicksContract {
    fun onBackClick()
    fun onRetryClick()
    fun onSaveClick(movieId: Int)
    fun onMovieClick(movieId: Int)
}
