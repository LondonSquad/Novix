@file:KoverIgnore

package com.london.data.mapper.details.actor

import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowCastMember
import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.cast.ActorMediaDetails
import com.london.domain.entity.actordetails.cast.ActorMediaItems

fun ActorTvShowDetailsResponse.toEntity(): ActorMediaDetails =
    ActorMediaDetails(
        mediaItems = cast?.map { it.toEntity() }.orEmpty(),
    )


fun ActorTvShowCastMember.toEntity(): ActorMediaItems =
    ActorMediaItems(
        id = id.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
    )
