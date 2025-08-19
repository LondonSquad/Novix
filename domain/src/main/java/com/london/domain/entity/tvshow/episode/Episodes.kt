package com.london.domain.entity.tvshow.episode

data class Episodes(
    val airDate: String?,
    val episodeNumber: Int,
    val episodeType: String,
    val id: Int,
    val name: String,
    val overview: String,
    val runtime: Int?,
    val seasonNumber: Int,
    val showId: Int,
    val imageUrl: String?,
    val voteAverage: Double,
)
