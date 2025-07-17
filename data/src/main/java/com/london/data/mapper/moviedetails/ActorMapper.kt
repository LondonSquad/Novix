package com.london.data.mapper.moviedetails

import com.london.data.datasource.remote.details.moviedetails.model.moviecast.MovieActor
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.Actor

fun MovieActor.toEntity(): Actor {
    return Actor(
        id = this.id.orZero(),
        name = this.originalName.orEmpty(),
        profilePicture = profilePath.asImageUrlOrEmpty(),
        characterName = this.character.orEmpty(),
    )
}
