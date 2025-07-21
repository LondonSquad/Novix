package com.london.data.datasource.remote.details.tvshowdetails.model.tvshowepisode

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowEpisodesRemoteResponse(
    @SerialName("_id")
    val id: String?,
    @SerialName("air_date")
    val airDate: String?,
    @SerialName("episodes")
    val episodes: List<TvShowEpisodeBySeason>?
)
