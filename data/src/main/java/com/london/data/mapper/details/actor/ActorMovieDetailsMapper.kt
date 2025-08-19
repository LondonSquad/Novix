package com.london.data.mapper.details.actor

import com.london.data.remote.model.details.actor.movie.ActorMovieCastMember
import com.london.data.remote.model.details.actor.movie.ActorMovieDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actor.cast.ActorMediaDetails
import com.london.domain.entity.actor.cast.ActorMediaItems

fun ActorMovieDetailsResponse.toEntity(): ActorMediaDetails {
    return ActorMediaDetails(
        mediaItems = cast.orEmpty().map { it.toEntity() },
    )
}

@KoverIgnore
fun ActorMovieCastMember.toEntity(): ActorMediaItems {
    return ActorMediaItems(
        id = id.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
    )
}
