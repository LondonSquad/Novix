package com.london.data.mapper.moviedetails

import com.london.data.datasource.remote.details.moviedetails.model.moviecast.MovieActor
import com.london.domain.entity.Actor


fun MovieActor.toEntity(): Actor {
    return Actor(
        id = this.id,
        name = this.originalName ,
        profilePicture = "https://image.tmdb.org/t/p/w500" + (this.profilePath ?: ""),
        characterName = this.character ,
    )
}
