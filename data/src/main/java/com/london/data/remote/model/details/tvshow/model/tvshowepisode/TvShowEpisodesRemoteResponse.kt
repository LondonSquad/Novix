package com.london.data.remote.model.details.tvshow.model.tvshowepisode

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowEpisodesRemoteResponse(
    @SerialName("_id")
    val id: String? = null,
    @SerialName("episodes")
    val episodes: List<TvShowEpisodeBySeason>? = null
)
