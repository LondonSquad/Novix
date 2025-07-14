package com.london.data.datasource.remote.details.tvshowdetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowImagesRemoteResponse(
    @SerialName("backdrops")
    val backdrops: List<ImageItem>,
    @SerialName("id")
    val id: Int,
    @SerialName("logos")
    val logos: List<ImageItem>,
    @SerialName("posters")
    val posters: List<ImageItem>
)

@Serializable
data class ImageItem(
    @SerialName("aspect_ratio")
    val aspectRatio: Double,
    @SerialName("height")
    val height: Int,
    @SerialName("iso_639_1")
    val iso6391: String?,
    @SerialName("file_path")
    val filePath: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("width")
    val width: Int
)