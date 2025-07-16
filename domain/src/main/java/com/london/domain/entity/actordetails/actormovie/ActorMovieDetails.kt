package com.london.domain.entity.actordetails.actormovie

data class ActorMovieDetails(
    val id: Int,
    val cast: List<ActorMovieCastMemberEntity>,
    val crew: List<ActorMovieCrewMemberEntity>
)