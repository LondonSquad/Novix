package com.london.domain.entity.actordetails

data class ActorTvShowDetails(
    val id: Int,
    val cast: List<ActorTvShowCastMemberEntity>,
    val crew: List<ActorTvShowCrewMemberEntity>
)

data class ActorTvShowCastMemberEntity(
    val adult: Boolean,
    val backdropPath: String,
    val character: String,
    val creditId: String,
    val episodeCount: Int,
    val firstAirDate: String,
    val firstCreditAirDate: String,
    val genreIds: List<Int>,
    val id: Int,
    val name: String,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val voteAverage: Double,
    val voteCount: Int
)


data class ActorTvShowCrewMemberEntity(
    val adult: Boolean,
    val backdropPath: String,
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
    val posterPath: String,
    val voteAverage: Double,
    val voteCount: Int
)
