package com.london.domain.entity.actordetails.actormovie

data class ActorMovieCastMemberEntity(
    val adult: Boolean = false,
    val backdropUrl: String = "",
    val character: String = "",
    val creditId: String = "",
    val genreIds: List<Int> = listOf(),
    val id: Int = 0,
    val order: Int = 0,
    val originalLanguage: String = "",
    val originalTitle: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterUrl: String = "",
    val releaseDate: String = "",
    val title: String = "",
    val video: Boolean = false,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0
)