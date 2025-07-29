package com.london.data.remote.model.details.tvshow.model.tvshowepisode


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeVideoResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("results")
    val results: List<EpisodeVideoProviderRemote>? = null
)
