package com.london.domain.entity

import com.london.domain.entity.genre.MovieGenre

data class UpComingMovie(
    val id: Int,
    val imageUrl: String,
    val genres: List<MovieGenre>,
)
