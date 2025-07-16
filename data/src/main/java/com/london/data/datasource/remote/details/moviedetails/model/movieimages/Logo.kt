package com.london.data.datasource.remote.details.moviedetails.model.movieimages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Logo(
    @SerialName("aspect_ratio")
    val aspectRatio: Double = 0.0,
    @SerialName("file_path")
    val filePath: String,
    val height: Int = 0,
    @SerialName("iso_639_1")
    val iso6391: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0,
    val width: Int = 0
)
