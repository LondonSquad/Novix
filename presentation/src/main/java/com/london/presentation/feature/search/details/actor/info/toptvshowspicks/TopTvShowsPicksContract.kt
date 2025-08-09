package com.london.presentation.feature.search.details.actor.info.toptvshowspicks

interface TopTvShowsPicksContract {
    fun onSaveTvShow(tvShowId: Int)
    fun onTvShowClicked(tvShowId: Int)
    fun onBack()
    fun onRetry()
}