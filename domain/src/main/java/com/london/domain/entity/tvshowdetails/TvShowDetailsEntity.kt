package com.london.domain.entity.tvshowdetails

data class TvShowDetailsEntity(
    val firstAirDate: String,
    val tvShowGenres: List<TvShowGenreEntity>,
    val id: Int,
    val name: String,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val overview: String,
    val posterUrl: String?,
    val tvShowSeasons: List<Int>,
    val voteAverage: Double,
)

data class TvShowGenreEntity(
    val id: Int,
    val name: String
)
