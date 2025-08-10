package com.london.domain.entity.actordetails.cast

data class CastDetails(
    val id: Int = 0,
    val cast: List<CastActorEntity> = emptyList(),
)
