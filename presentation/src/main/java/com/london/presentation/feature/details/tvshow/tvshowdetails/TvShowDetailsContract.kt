package com.london.presentation.feature.details.tvshow.tvshowdetails

interface TvShowDetailsContract {
    fun onBackClicked()
    fun onEpisodeDetailsClicked(tvShowId: Int, episodeNumber: Int, seasonNumber: Int)
    fun onReviewsClicked(tvShowId: Int, mediaType: Int)
    fun onCastClicked(tvShowId: Int)
    fun OnGenreClicked(genreId: Int)
    fun onEpisodeClick(tvShowId: Int, episodeNumber: Int, seasonNumber: Int)
}