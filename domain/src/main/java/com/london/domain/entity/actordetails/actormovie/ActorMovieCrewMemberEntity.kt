package com.london.domain.entity.actordetails.actormovie

data class ActorMovieCrewMemberEntity(
    val adult: Boolean,
    val backdropPath: String,
    val creditId: String,
    val department: String,
    val genreIds: List<Int>,
    val id: Int,
    val job: String,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)