package com.london.domain.entity.trending

data class TrendingMovie(
    val id: Int,
    val posterPath: String,
    val genreIds: List<Int>
) 