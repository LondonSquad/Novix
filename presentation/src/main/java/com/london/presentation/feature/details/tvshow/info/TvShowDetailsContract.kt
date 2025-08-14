package com.london.presentation.feature.details.tvshow.info

import com.london.presentation.shared.genre.TvShowGenreUi

interface TvShowDetailsContract {
    fun onBackClicked()
    fun onEpisodeClicked(tvShowId: Int, episodeNumber: Int, seasonNumber: Int)
    fun onReviewsClicked(tvShowId: Int, mediaType: Int)
    fun onCastClicked(tvShowId: Int)
    fun onGenreClicked(genre: TvShowGenreUi)
    fun onRateBottomSheetClick()
    fun onSelectRatingClick(rating: Int)
    fun onLoginClick()
}