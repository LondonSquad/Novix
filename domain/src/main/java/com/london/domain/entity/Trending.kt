package com.london.domain.entity

data class Trending(
    val id: Int,
    val title: String,
    val posterPath: String,
    val genreIds: List<Int>
)
