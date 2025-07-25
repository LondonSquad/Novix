package com.london.data.mapper.moviedetails

import com.london.data.remote.model.details.movie.model.moviecast.MovieActor
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.Actor

fun MovieActor.toEntity(): Actor {
    return Actor(
        id = id.orZero(),
        name = originalName.orEmpty(),
        profilePicture = profilePath.asImageUrlOrEmpty(),
        characterName = character.orEmpty(),
    )
}
