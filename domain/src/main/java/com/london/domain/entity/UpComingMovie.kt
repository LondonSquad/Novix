package com.london.domain.entity

data class UpComingMovie(
    val id: Int,
    val imageUrl: String,
    val genreIds: List<Int>,
)
