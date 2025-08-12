package com.london.presentation.feature.details.actor.info.toptvshowspicks

interface TopTvShowsPicksContract {
    fun onBackClick()
    fun onRetryClick()
    fun onSaveClick(tvShowId: Int)
    fun onTvShowClick(tvShowId: Int)
}
