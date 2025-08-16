package com.london.data.remote.model.details.videoprovider

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoRemote(
    @SerialName("key")
    val key: String? = null,
)