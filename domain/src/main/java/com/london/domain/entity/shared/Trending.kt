package com.london.domain.entity.shared

import com.london.domain.entity.genre.Genre

data class Trending(
    val id: Int,
    val title: String,
    val posterPath: String,
    val genres: List<Genre>
)
