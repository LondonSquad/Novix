package com.london.data.remote.model.details.tvshow.model

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

@Serializable
data class ImageItem(
    @SerialName("file_path")
    val filePath: String? = null,
)