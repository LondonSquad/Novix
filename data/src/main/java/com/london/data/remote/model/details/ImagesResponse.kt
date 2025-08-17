package com.london.data.remote.model.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponse(
    @SerialName("backdrops")
    val backdrops: List<ImageRemote>? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("logos")
    val logos: List<ImageRemote>? = null,
    @SerialName("posters")
    val posters: List<ImageRemote>? = null
)