package com.london.data.remote.model.details.tvshow.episode

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeasonEpisodesResponse(
    @SerialName("_id")
    val seasonId: String? = null,
    @SerialName("episodes")
    val episodes: List<Episode>? = null
)
