package com.london.presentation.screen.details.tvshow.episodedetails

import com.london.domain.entity.tvshowdetails.ImageItemEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity

data class EpisodeDetailsUiState(
    val tvImages: List<ImageItemEntity>? = listOf(),
    val tvShowEpisode: TvShowEpisodeByIdEntity = TvShowEpisodeByIdEntity(
            airDate = "",
            episodeNumber = 0,
            seasonNumber = 0,
            episodeTypes = "",
            tvShowId = 0,
            name = "",
            overview = "",
            stillPath = "",
            voteAverage = 0.0,
            voteCount = 0,
            guestStars = listOf(),
            id = 0,
    ),
    val backdropPath: String? = "",
){
    val hasEpisodeData: Boolean get() = tvShowEpisode.id != 0 && tvShowEpisode.name.isNotEmpty()
    val hasImages: Boolean get() = !tvImages.isNullOrEmpty()
    val hasGuestStars: Boolean get() = tvShowEpisode.guestStars.isNotEmpty()
    val hasOverview: Boolean get() = tvShowEpisode.overview.isNotEmpty()
}
