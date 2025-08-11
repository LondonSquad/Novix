package com.london.domain.entity.toprated

data class TopRatedMedia(
    val id: Int,
    val posterUrl: String,
    val releaseDate: String,
    val name: String,
    val voteAverage: Double,
    val genreIds: List<Int>
)