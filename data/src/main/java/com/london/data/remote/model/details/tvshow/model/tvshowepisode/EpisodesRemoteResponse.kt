package com.london.data.remote.model.details.tvshow.model.tvshowepisode

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodesRemoteResponse(
    @SerialName("_id")
    val id: String? = null,
    @SerialName("episodes")
    val episodes: List<EpisodeBySeason>? = null
)
