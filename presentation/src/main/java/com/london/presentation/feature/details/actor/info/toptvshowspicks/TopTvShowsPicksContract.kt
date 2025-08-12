package com.london.presentation.feature.details.actor.info.toptvshowspicks

interface TopTvShowsPicksContract {
    fun onRetry()
    fun onBackClick()
    fun onSaveClick(tvShowId: Int)
    fun onTvShowClick(tvShowId: Int)
}
