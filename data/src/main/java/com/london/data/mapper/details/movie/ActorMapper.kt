package com.london.data.mapper.details.movie

import com.london.data.remote.model.details.movie.cast.MovieActor
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.actor.Actor

fun MovieActor.toEntity(): Actor {
    return Actor(
        id = id.orZero(),
        name = originalName.orEmpty(),
        profilePictureUrl = profilePath.asImageUrlOrEmpty(),
        characterName = character.orEmpty(),
    )
}
