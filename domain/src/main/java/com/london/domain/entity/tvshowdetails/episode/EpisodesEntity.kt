package com.london.domain.entity.tvshowdetails.episode

data class EpisodesEntity(
    val id: String,
    val episodes: List<EpisodeBySeasonEntity>
)