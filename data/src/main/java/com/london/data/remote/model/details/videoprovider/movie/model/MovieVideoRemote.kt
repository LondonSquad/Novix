package com.london.data.remote.model.details.videoprovider.movie.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideoRemote(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("results")
    val movies: List<MovieVideoRemoteResponse>? = null
)

@Serializable
data class MovieVideoRemoteResponse(
    @SerialName("key")
    val key: String? = null,
)