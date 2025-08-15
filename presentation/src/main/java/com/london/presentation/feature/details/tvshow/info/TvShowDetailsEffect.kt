package com.london.presentation.feature.details.tvshow.info

import com.london.domain.entity.recent.MediaType

sealed interface TvShowDetailsEffect {
    data class OnNavigateToEpisodeDetails(
        val tvShowId: Int,
        val episodeNumber: Int,
        val seasonNumber: Int
    ) : TvShowDetailsEffect

    data object NavigateBack : TvShowDetailsEffect
    data object OnLoginNavigation : TvShowDetailsEffect
    data class NavigateToCast(val tvShowId: Int) : TvShowDetailsEffect
    data class NavigateToTvShowsByCategoryId(val categoryId: Int) : TvShowDetailsEffect
    data class NavigateToReviews(val tvShowId: Int, val mediaType: MediaType) : TvShowDetailsEffect
}