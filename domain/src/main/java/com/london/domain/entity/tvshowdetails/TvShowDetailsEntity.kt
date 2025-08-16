package com.london.domain.entity.tvshowdetails

import com.london.domain.entity.genre.TvShowGenre

data class TvShowDetailsEntity(
    val firstAirDate: String,
    val tvShowGenres: List<TvShowGenre>,
    val id: Int,
    val name: String,
    val numberOfEpisodes: Int,
    val numberOfSeasons: Int,
    val overview: String,
    val posterUrl: String?,
    val tvShowSeasons: List<Int>,
    val voteAverage: Double,
)
