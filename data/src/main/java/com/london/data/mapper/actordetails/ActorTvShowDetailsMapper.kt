@file:KoverIgnore
package com.london.data.mapper.actordetails

import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowCastMember
import com.london.data.remote.model.details.actor.model.actortvshowdetails.ActorTvShowDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowCastMemberEntity
import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails

fun ActorTvShowDetailsResponse.toEntity(): ActorTvShowDetails {
    return ActorTvShowDetails(
        id = id.orZero(),
        cast = cast?.map { it.toEntity() }.orEmpty(),
    )
}

fun ActorTvShowCastMember.toEntity(): ActorTvShowCastMemberEntity {
    return ActorTvShowCastMemberEntity(
        id = id.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
    )
}
