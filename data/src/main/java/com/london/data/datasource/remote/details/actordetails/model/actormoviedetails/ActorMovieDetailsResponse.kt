package com.london.data.datasource.remote.details.actordetails.model.actormoviedetails

import kotlinx.serialization.SerialName

data class ActorMovieDetailsResponse(
    @SerialName("cast")
    val cast: List<ActorMovieCastMember>,
    @SerialName("crew")
    val crew: List<MovieCrewMember>,
    @SerialName("id")
    val id: Int
)