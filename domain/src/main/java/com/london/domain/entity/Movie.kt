package com.london.domain.entity

data class Movie(
    val id: Int,
    val name: String,
    val posterUrl: String,
    val releaseYear: Int,
    val rating: Int,
    val genreIds: List<Int>,
)
