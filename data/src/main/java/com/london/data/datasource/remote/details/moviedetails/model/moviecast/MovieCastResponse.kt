package com.london.data.datasource.remote.details.moviedetails.model.moviecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCastResponse(
    val id: Int,
    @SerialName("cast")
    val actorRemote: List<MovieActor>,
    val crew: List<Crew>
)
