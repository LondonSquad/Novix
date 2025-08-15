package com.london.data.remote.model.details.movie.model.movieimages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Logo(
    @SerialName("file_path")
    val filePath: String?,
)
