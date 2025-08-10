package com.london.presentation.feature.details.tvshow.episode

interface EpisodeDetailsContract {
    fun onBackClicked()
    fun onLoginClick()
    fun onRateBottomSheetClick()
    fun onSelectRatingClick(rating: Int)
}