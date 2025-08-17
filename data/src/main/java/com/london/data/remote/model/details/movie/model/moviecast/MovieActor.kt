package com.london.data.remote.model.details.movie.model.moviecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieActor(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("original_name")
    val originalName: String? = null,
    @SerialName("profile_path")
    val profilePath: String? = null,
    @SerialName("character")
    val character: String? = null,
)