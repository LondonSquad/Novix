package com.london.domain.entity.tvshowdetails.episode

data class TvShowEpisodesEntity(
    val id: String,
    val airDate: String?,
    val episodes: List<TvShowEpisodeBySeasonEntity>
)