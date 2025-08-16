package com.london.data.remote.model.details.videoprovider

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoTrailerRemote(
    @SerialName("key")
    val youtubeKey: String? = null,
)
