package com.london.data.datasource.remote.details.videoprovider.tvshow

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowVideoResponse(
    val id: Int,
    @SerialName("results")
    val tvShow: List<TvShowVideoRemote>
)