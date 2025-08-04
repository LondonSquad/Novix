package com.london.domain.entity

data class MovieList(
    val id: UInt,
    val name: String,
    val moviesCount: UInt = 0u
)