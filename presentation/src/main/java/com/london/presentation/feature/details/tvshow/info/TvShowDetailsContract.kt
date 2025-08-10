package com.london.presentation.feature.details.tvshow.info

interface TvShowDetailsContract {
    fun onBackClicked()
    fun onEpisodeClicked(tvShowId: Int, episodeNumber: Int, seasonNumber: Int)
    fun onReviewsClicked(tvShowId: Int, mediaType: Int)
    fun onCastClicked(tvShowId: Int)
    fun OnGenreClicked(genreId: Int)
    fun onRateBottomSheetClick()
    fun onSelectRatingClick(rating: Int)
    fun onLoginClick()
}