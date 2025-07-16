package com.london.data.datasource.remote.details.actordetails.model.actorimage

import kotlinx.serialization.SerialName

data class ActorImageResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("profiles")
    val profiles: List<ProfileDto>
)