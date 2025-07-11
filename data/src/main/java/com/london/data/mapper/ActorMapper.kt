package com.london.data.mapper

import com.london.data.datasource.local.model.PersonDtoLocal
import com.london.data.datasource.remote.search.PersonDto
import com.london.domain.entity.Actor

fun PersonDto.toActorEntity(): Actor {
    return Actor(
        id = this.id,
        name = this.name,
        profilePicture = this.profilePath ?: ""
    )
}

fun PersonDtoLocal.toActorEntity(): Actor {
    return Actor(
        id = this.id,
        name = this.name,
        profilePicture = this.profilePath ?: ""
    )
}