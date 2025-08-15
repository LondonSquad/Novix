package com.london.domain.entity.moviedatails

import com.london.domain.entity.genre.MovieGenre

data class MovieDetails(
    val backdropUrl: String,
    val genres: List<MovieGenre>,
    val id: Int,
    val overview: String,
    val posterUrl: String,
    val releaseDate: String,
    val runtime: Int,
    val title: String,
    val video: Boolean,
    val voteAverage: String,
)