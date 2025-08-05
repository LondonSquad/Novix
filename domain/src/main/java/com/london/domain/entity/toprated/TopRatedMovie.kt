package com.london.domain.entity.toprated

data class TopRatedMovie(
    val id: Int,
    val posterUrl: String,
    val releaseDate: String,
    val title: String,
    val voteAverage: Double,
    val genreIds: List<Int>
)