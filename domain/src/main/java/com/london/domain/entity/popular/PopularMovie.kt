package com.london.domain.entity.popular

data class PopularMovie(
    val id: Int,
    val title: String,
    val posterPath: String,
    val backdropPath: String,
    val voteAverage: Double,
)