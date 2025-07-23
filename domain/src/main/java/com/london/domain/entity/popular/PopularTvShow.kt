package com.london.domain.entity.popular

data class PopularTvShow(
    val id: Int,
    val name: String,
    val overview: String,
    val posterUrl: String,
    val rating: Double
)
