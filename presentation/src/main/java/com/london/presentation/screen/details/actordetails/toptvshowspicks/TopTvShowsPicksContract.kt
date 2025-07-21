package com.london.presentation.screen.details.actordetails.toptvshowspicks

interface TopTvShowsPicksContract {
    fun onSaveMovie(movieId: Int)
    fun onBackClicked()
    fun onTvShowClicked(tvShowId: Int)
}