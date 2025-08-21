package com.london.presentation.feature.details.tvshow.episode

interface EpisodeDetailsContract {
    fun onBackClick()
    fun onLoginClick(tvShowId: Int, seasonNumber: Int, episodeNumber: Int)
    fun onRateEpisodeClick()
    fun onSelectRatingClick(rating: Int)
}
