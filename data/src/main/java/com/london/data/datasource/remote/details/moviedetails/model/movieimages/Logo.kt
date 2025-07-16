package com.london.data.datasource.remote.details.moviedetails.model.movieimages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Logo(
    @SerialName("aspect_ratio")
    val aspectRatio: Double,
    @SerialName("file_path")
    val filePath: String,
    val height: Int,
    @SerialName("iso_639_1")
    val iso6391: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    val width: Int
)