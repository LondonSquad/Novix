package com.london.domain.entity

import com.london.domain.entity.genre.MovieGenre

data class Movie(
    val id: Int,
    val name: String,
    val posterUrl: String,
    val releaseYear: Int,
    val rating: Int,
    val genres: List<MovieGenre>,
)
