package com.london.domain.entity.tvshowdetails

data class TvShowEpisodeEntity(
    val id: Int,
    val name: String,
    val overview: String,
    val voteAverage: Double,
    val voteCount: Int,
    val airDate: String,
    val episodeNumber: Int,
    val episodeType: String,
    val productionCode: String,
    val runtime: Int?,
    val seasonNumber: Int,
    val showId: Int,
    val stillPath: String?
)
