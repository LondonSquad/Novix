package com.london.domain.entity.tvshowdetails.episode

data class SeasonEpisodes(
    val seasonId: String,
    val episodes: List<Episodes>
)