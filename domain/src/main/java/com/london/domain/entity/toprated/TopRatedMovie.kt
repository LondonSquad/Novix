package com.london.domain.entity.toprated

data class TopRatedMovie(
    val adult: Boolean,
    val backdropUrl: String,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterUrl: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val genreIds: List<Int>
)