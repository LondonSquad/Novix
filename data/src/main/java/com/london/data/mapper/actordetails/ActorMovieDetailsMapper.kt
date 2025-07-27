package com.london.data.mapper.actordetails

import com.london.data.remote.model.details.actor.model.actormoviedetails.ActorMovieCastMember
import com.london.data.remote.model.details.actor.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.actormovie.ActorMovieCastMemberEntity
import com.london.domain.entity.actordetails.actormovie.ActorMovieDetails

fun ActorMovieDetailsResponse.toEntity(): ActorMovieDetails {
    return ActorMovieDetails(
        id = id.orZero(),
        cast = cast?.map { it.toEntity() }.orEmpty(),
    )
}

@KoverIgnore
fun ActorMovieCastMember.toEntity(): ActorMovieCastMemberEntity {
    return ActorMovieCastMemberEntity(
        id = id.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
    )
}
