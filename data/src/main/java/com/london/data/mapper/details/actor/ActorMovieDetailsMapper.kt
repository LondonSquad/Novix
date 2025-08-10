package com.london.data.mapper.details.actor

import com.london.data.remote.model.details.actor.model.actormoviedetails.ActorMovieCastMember
import com.london.data.remote.model.details.actor.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.cast.CastActorEntity
import com.london.domain.entity.actordetails.cast.CastDetails

fun ActorMovieDetailsResponse.toEntity(): CastDetails {
    return CastDetails(
        id = id.orZero(),
        cast = cast.orEmpty().map { it.toEntity() },
    )
}

@KoverIgnore
fun ActorMovieCastMember.toEntity(): CastActorEntity {
    return CastActorEntity(
        id = id.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
    )
}
