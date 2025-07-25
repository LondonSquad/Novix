package com.london.data.remote.model.details.movie.model.moviecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCastResponse(
    @SerialName("id")
    val id: Int?,
    @SerialName("cast")
    val actorRemote: List<MovieActor>?,
    @SerialName("crew")
    val crew: List<Crew>?
)
