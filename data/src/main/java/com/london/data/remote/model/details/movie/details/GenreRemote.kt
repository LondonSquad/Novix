package com.london.data.remote.model.details.movie.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenreRemote(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null
)
