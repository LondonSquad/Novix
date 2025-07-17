package com.london.data.datasource.remote.details.actordetails.model.actorimage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("aspect_ratio")
    val aspectRatio: Double,
    @SerialName("height")
    val height: Int,
    @SerialName("iso_639_1")
    val iso: String?,
    @SerialName("file_path")
    val filePath: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    @SerialName("width")
    val width: Int
)
