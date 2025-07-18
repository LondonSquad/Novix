package com.london.domain.entity.actordetails.actortvshow

data class ActorTvShowCrewMemberEntity(
    val adult: Boolean,
    val backdropUrl: String,
    val creditId: String,
    val department: String,
    val episodeCount: Int,
    val firstAirDate: String,
    val firstCreditAirDate: String,
    val genreIds: List<Int>,
    val id: Int,
    val job: String,
    val name: String,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val popularity: Double,
    val posterUrl: String,
    val voteAverage: Double,
    val voteCount: Int
)