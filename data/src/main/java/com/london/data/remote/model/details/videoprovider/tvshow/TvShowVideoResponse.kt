package com.london.data.remote.model.details.videoprovider.tvshow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowVideoResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("results")
    val tvShow: List<TvShowVideoRemote>?
)

@Serializable
data class TvShowVideoRemote(
    @SerialName("id")
    val id: String?,
    @SerialName("key")
    val key: String?,
    @SerialName("name")
    val name: String?,
)
