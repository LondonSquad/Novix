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
    val tvShowSeasons: List<TvShowSeasonEntity>,
    val voteAverage: Double,
)

data class TvShowGenreEntity(
    val id: Int,
    val name: String
)

data class TvShowSeasonEntity(
    val airDate: String?,
    val episodeCount: Int,
    val id: Int,
    val name: String,
    val overview: String,
    val posterUrl: String?,
    val seasonNumber: Int,
    val voteAverage: Double
)
