package com.london.presentation.feature.details.tvshow.info

import com.london.domain.entity.recent.MediaType

interface TvShowDetailsContract {
    fun onBackClicked()
    fun onEpisodeClicked(tvShowId: Int, episodeNumber: Int, seasonNumber: Int)
    fun onReviewsClicked(tvShowId: Int, mediaType: MediaType)
    fun onCastClicked(tvShowId: Int)
    fun onGenreClicked(genreId: Int)
    fun onRateBottomSheetClick()
    fun onSelectRatingClick(rating: Int)
    fun onLoginClick()
}