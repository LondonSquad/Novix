package com.london.domain.entity

data class MovieList(
    val id: Int,
    val name: String,
    val moviesCount: Int = 0
)