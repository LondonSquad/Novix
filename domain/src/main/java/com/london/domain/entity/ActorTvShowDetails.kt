package com.london.domain.entity

data class ActorTvShowDetails(
    val id: Int,
    val name: String,
    val adult: Boolean,
    val backdropPath: String,
    val character: String,
    val creditId: String,
    val episodeCount: Int,
    val firstAirDate: String,
    val firstCreditAirDate: String,
    val genreIds: List<Int>,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val voteAverage: Double,
    val voteCount: Int
)
