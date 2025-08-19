package com.london.data.remote.model.details.actor.movie

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorMovieCastMember(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
)
