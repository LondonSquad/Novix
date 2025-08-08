package com.london.presentation.feature.details.actordetails.toptvshowspicks

interface TopTvShowsPicksContract {
    fun onSaveTvShow(tvShowId: Int)
    fun onTvShowClicked(tvShowId: Int)
    fun onBack()
    fun onRetry()
}