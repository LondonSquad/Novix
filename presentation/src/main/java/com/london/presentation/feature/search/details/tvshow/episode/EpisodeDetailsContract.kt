package com.london.presentation.feature.search.details.tvshow.episode

interface EpisodeDetailsContract {
    fun onBackClicked()
    fun onLoginClick()
    fun onRateBottomSheetClick()
    fun onSelectRatingClick(rating: Int)
}