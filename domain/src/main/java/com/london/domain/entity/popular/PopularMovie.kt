package com.london.domain.entity.popular

data class PopularMovie(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val backdropUrl: String,
    val rating: Double,
)