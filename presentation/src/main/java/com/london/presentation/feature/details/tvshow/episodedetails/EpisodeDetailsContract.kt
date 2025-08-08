package com.london.presentation.feature.details.tvshow.episodedetails

interface EpisodeDetailsContract {
    fun onBackClicked()
    fun onLoginClick()
    fun onRateBottomSheetClick()
    fun onSelectRatingClick(rating: Int)
    fun onNavigateToCast(actorId: Int)
}