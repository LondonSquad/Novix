package com.london.domain.entity

data class Movie(
    val id: Int,
    val name: String,
    val posterPicture: String,
    val releaseYear: Int,
    val rating: Int,
    val genreIds: List<Int>,
)
