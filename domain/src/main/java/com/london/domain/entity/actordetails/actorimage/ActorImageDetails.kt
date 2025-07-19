package com.london.domain.entity.actordetails.actorimage


data class ActorImageDetails(
    val id: Int = 0,
    val profiles: List<ImageDetails> = emptyList()
)