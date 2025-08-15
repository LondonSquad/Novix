package com.london.data.remote.model.details.tvshow.model.tvshowepisode

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeVideoProviderRemote(
    @SerialName("key")
    val key: String? = null,
)