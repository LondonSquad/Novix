package com.london.data.mapper.details.actor

import com.london.data.remote.model.details.actor.movie.ActorMovieCastMember
import com.london.data.remote.model.details.actor.movie.ActorMovieDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.actor.ActorMediaDetails
import com.london.domain.entity.actor.ActorMediaItems

fun ActorMovieDetailsResponse.toEntity(): ActorMediaDetails =
     ActorMediaDetails(
        mediaItems = cast.orEmpty().map { it.toEntity() },
    )


fun ActorMovieCastMember.toEntity(): ActorMediaItems =
     ActorMediaItems(
        id = id.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
    )

