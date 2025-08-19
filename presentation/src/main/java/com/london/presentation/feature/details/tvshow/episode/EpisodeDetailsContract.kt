package com.london.presentation.feature.details.tvshow.episode

interface EpisodeDetailsContract {
    fun onBackClick()
    fun onLoginClick()
    fun onRateEpisodeClick()
    fun onSelectRatingClick(rating: Int)
}