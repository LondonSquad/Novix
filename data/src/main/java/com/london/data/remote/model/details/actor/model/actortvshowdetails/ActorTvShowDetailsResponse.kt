package com.london.data.remote.model.details.actor.model.actortvshowdetails

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorTvShowDetailsResponse(
    @SerialName("cast")
    val cast: List<ActorTvShowCastMember>?,
    @SerialName("crew")
    val crew: List<ActorTvShowCrewMember>?,
    @SerialName("id")
    val id: Int?
)
