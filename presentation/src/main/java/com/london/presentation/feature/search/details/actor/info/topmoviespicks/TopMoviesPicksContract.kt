package com.london.presentation.feature.search.details.actor.info.topmoviespicks

interface TopMoviesPicksContract {
    fun onSaveMovie(movieId: Int)
    fun onMovieClicked(movieId: Int)
    fun onBack()
    fun onRetry()
}