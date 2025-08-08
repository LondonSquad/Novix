package com.london.data.mapper.details.actor

import com.london.data.remote.model.details.actor.model.ActorDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.ActorDetails

@KoverIgnore
fun ActorDetailsResponse.toEntity(): ActorDetails {
    return ActorDetails(
        id = id.orZero(),
        name = name.orEmpty(),
        birthday = birthday.orEmpty(),
        deathDay = deathDay,
        placeOfBirth = placeOfBirth.orEmpty(),
        biography = biography.orEmpty(),
        knownForDepartment = knownForDepartment.orEmpty(),
        profileUrl = profilePath.asImageUrlOrEmpty()
    )
}


