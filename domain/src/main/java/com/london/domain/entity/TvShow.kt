package com.london.domain.entity

data class TvShow(
    val id: Int,
    val name: String,
    val posterPicture: String,
    val releaseYear: Int,
    val rating: Int,
    val genres: List<Int>
)
