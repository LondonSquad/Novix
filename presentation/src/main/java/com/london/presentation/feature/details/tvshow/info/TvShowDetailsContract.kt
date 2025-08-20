package com.london.presentation.feature.details.tvshow.info

import com.london.domain.entity.shared.MediaType
import com.london.presentation.shared.genre.TvShowGenreUi

interface TvShowDetailsContract {
    fun onLoginClick()
    fun onBackClicked()
    fun onRateBottomSheetClick()
    fun onCastClicked(tvShowId: Int)
    fun onSelectRatingClick(rating: Int)
    fun onGenreClicked(genre: TvShowGenreUi)
    fun onReviewsClicked(tvShowId: Int, mediaType: MediaType)
    fun onEpisodeClicked(tvShowId: Int, episodeNumber: Int, seasonNumber: Int)
}
