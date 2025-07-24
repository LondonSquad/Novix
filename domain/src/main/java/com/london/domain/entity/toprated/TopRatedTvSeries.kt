package com.london.domain.entity.toprated


data class TopRatedTvSeries(
    val adult: Boolean,
    val backdropUrl: String,
    val firstAirDate: String,
    val genreIds: List<Int>,
    val id: Int,
    val name: String,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val popularity: Double,
    val posterUrl: String,
    val voteAverage: Double,
    val voteCount: Int
)