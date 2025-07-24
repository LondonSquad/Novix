package com.london.domain.entity.trending

data class TrendingTvShow(
    val id: Int,
    val posterPath: String,
    val genreIds: List<Int>
) 