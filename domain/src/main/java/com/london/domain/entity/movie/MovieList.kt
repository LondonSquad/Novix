package com.london.domain.entity.movie

data class MovieList(
    val id: Int,
    val name: String,
    val moviesCount: Int = 0
)