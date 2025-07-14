package com.london.domain.entity

data class ActorMovieDetails(
    val id: Int,
    val title: String,
    val adult: Boolean,
    val backdropPath: String,
    val character: String,
    val creditId: String,
    val genreIds: List<Int>,
    val order: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)