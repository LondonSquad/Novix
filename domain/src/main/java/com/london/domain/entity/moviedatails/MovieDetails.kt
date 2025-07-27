package com.london.domain.entity.moviedatails

data class MovieDetails(
    val backdropUrl: String,
    val genresId: List<Int>,
    val id: Int,
    val overview: String,
    val posterUrl: String,
    val releaseDate: String,
    val runtime: Int,
    val title: String,
    val video: Boolean,
    val voteAverage: String,
)