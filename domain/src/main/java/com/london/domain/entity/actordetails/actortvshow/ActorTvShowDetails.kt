package com.london.domain.entity.actordetails.actortvshow

data class ActorTvShowDetails(
    val id: Int,
    val cast: List<ActorTvShowCastMemberEntity>,
    val crew: List<ActorTvShowCrewMemberEntity>
)