package com.london.data.remote.model.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponse(
    @SerialName("backdrops")
    val backdrops: List<ImageItem>?,
    @SerialName("id")
    val id: Int?,
    @SerialName("logos")
    val logos: List<ImageItem>?,
    @SerialName("posters")
    val posters: List<ImageItem>?
)