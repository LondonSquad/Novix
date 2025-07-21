package com.london.presentation.screen.details.tvshow.tvshowdetails

sealed interface TvShowDetailsEffect {
    data class OnNavigateToEpisodeDetails(
        val tvShowId: Int,
        val episodeNumber: Int,
        val seasonNumber: Int
    ) : TvShowDetailsEffect

    data object NavigateBack : TvShowDetailsEffect
    data class NavigateToReviews(val tvShowId: Int, val mediaType: Int) : TvShowDetailsEffect
    data class NavigateToCast(val tvShowId: Int) : TvShowDetailsEffect

}