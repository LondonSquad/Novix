package com.london.data.remote.model.details.videoprovider

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("results")
    val videos: List<VideoTrailerRemote>? = null
)