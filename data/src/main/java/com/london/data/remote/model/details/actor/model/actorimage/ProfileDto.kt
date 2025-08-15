package com.london.data.remote.model.details.actor.model.actorimage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("file_path")
    val filePath: String? = null,
)