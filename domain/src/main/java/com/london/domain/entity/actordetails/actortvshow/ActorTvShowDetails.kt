package com.london.domain.entity.actordetails.actortvshow

data class ActorTvShowDetails(
    val id: Int = 0,
    val cast: List<ActorTvShowCastMemberEntity> = emptyList(),
)