package com.london.data.remote.model.myrating

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RatingMediaResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("rating")
    val rating: Double? = null
)