package com.london.data.remote.model.details.actor.image

import com.london.data.remote.model.details.image.ImageRemote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorImageResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("profiles")
    val profiles: List<ImageRemote>? = null
)
