package com.london.domain.entity.movie

import com.london.domain.entity.genre.MovieGenre

data class UpComingMovie(
    val id: Int,
    val imageUrl: String,
    val genres: List<MovieGenre>,
)
