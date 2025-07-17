package com.london.data.datasource.remote.details.moviedetails.model.moviecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Backdrop(
    @SerialName("aspect_ratio")
    val aspectRatio: Double?,
    val height: Int?,
    @SerialName("iso_639_1")
    val iso6391: String??,
    @SerialName("file_path")
    val filePath: String?,
    @SerialName("vote_average")
    val voteAverage: Double?,
    @SerialName("vote_count")
    val voteCount: Int?,
    val width: Int?
)
