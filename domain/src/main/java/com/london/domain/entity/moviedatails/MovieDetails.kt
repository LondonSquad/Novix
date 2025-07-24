package com.london.domain.entity.moviedatails

import com.london.domain.entity.Actor
import com.london.domain.entity.Genre

data class MovieDetails(
    val movieId: Int,
    val movieImage: List<String>,
    val movieName: String,
    val movieRating: String,
    val movieDuration: String,
    val releaseDate: String,
    val movieOverview: String,
    val genres: List<Genre>,
    val actors: List<Actor>,
    val similarMovies: List<SimilarMovie>,
    val movieHaveTrailer: Boolean,
)