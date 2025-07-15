package com.london.data.mapper.moviedetails

import com.london.data.datasource.remote.moviedetails.model.ActorRemote
import com.london.domain.entity.Actor

fun ActorRemote.toActor(): Actor {
    return Actor(
        id = this.id,
        name = this.originalName ,
        profilePicture = "https://image.tmdb.org/t/p/w500" + (this.profilePath ?: ""),
        characterName = this.character ,
    )
}
