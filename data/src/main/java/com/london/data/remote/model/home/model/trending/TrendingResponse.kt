package com.london.data.remote.model.home.model.trending

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingResponse(
    @SerialName("id")
    val id: Int?,
    @SerialName("title")
    val title: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("profile_path")
    val profilePath: String?,
    @SerialName("genre_ids")
    val genreIds: List<Int>?
)
