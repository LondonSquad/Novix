package com.london.presentation.feature.details.actor.info.topmoviespicks

interface TopMoviesPicksContract {
    fun onSaveMovie(movieId: Int)
    fun onMovieClicked(movieId: Int)
    fun onBack()
    fun onRetry()
}