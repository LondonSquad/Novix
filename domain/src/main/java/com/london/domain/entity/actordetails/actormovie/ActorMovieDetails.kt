package com.london.domain.entity.actordetails.actormovie

data class ActorMovieDetails(
    val id: Int = 0,
    val cast: List<ActorMovieCastMemberEntity> = emptyList(),
    val crew: List<ActorMovieCrewMemberEntity> = emptyList()
)