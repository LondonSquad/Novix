package com.london.data.remote.model.details.videoprovider.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieVideoRemote(
    @SerialName("id")
    val id: Int?,
    @SerialName("results")
    val movies: List<MovieVideoRemoteResponse>?
)

@Serializable
data class MovieVideoRemoteResponse(
    @SerialName("id")
    val id: String?,
    @SerialName("key")
    val key: String?,
)
