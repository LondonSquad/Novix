package com.london.data.remote.model.details.tvshow.model

import com.london.data.remote.model.details.ImageItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowImagesRemoteResponse(
    @SerialName("backdrops")
    val backdrops: List<ImageItem>?,
    @SerialName("id")
    val id: Int?,
    @SerialName("logos")
    val logos: List<ImageItem>?,
    @SerialName("posters")
    val posters: List<ImageItem>?
)