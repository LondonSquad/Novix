package com.london.data.datasource.remote.details.videoprovider.movie.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideoResponse(
    @SerialName("id")
    val id: Int?,
    @SerialName("results")
    val movies: List<MovieVideoRemote>?
)