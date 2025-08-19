package com.london.data.remote.model.details.movie.cast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCastResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("cast")
    val actorRemote: List<MovieActor>? = null,
)
