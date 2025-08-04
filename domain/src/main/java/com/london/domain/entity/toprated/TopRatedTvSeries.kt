package com.london.domain.entity.toprated


data class TopRatedTvSeries(
    val id: Int,
    val name: String,
    val firstAirDate: String,
    val genreIds: List<Int>,
    val posterUrl: String,
    val voteAverage: Double,
)