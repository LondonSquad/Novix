package com.london.presentation.feature.details.actordetails.topmoviespicks

interface TopMoviesPicksContract {
    fun onSaveMovie(movieId: Int)
    fun onBack()
    fun onRetry()
}