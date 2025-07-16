package com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails

import kotlinx.serialization.SerialName

data class ActorTvShowDetailsResponse(
    @SerialName("cast")
    val cast: List<ActorTvShowCastMember>,
    @SerialName("crew")
    val crew: List<ActorTvShowCrewMember>,
    @SerialName("id")
    val id: Int
)