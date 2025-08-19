package com.london.data.remote.model.details.actor.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorMovieDetailsResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("cast")
    val cast: List<ActorMovieCastMember>? = null
)
