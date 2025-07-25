package com.london.domain.entity.trending

data class Trending(
    val id: Int,
    val posterPath: String,
    val genreIds: List<Int>
) 