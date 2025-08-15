package com.london.data.remote.model.details.videoprovider.tvshow.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowVideoResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("results")
    val tvShow: List<TvShowVideoRemote>? = null
)

@Serializable
data class TvShowVideoRemote(
    @SerialName("key")
    val key: String? = null,
)