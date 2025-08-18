package com.london.domain.entity.tvshow.episode

data class SeasonEpisodes(
    val seasonId: String,
    val episodes: List<Episodes>
)