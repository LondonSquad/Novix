@file:KoverIgnore

package com.london.data.mapper.details.actor

import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowCastMember
import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.domain.entity.actordetails.cast.CastActorEntity

fun ActorTvShowDetailsResponse.toEntity(): CastDetails =
    CastDetails(
        id = id.orZero(),
        cast = cast?.map { it.toEntity() }.orEmpty(),
    )


fun ActorTvShowCastMember.toEntity(): CastActorEntity =
    CastActorEntity(
        id = id.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
    )
