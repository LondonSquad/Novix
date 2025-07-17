package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.ActorDetails

@KoverIgnore
fun ActorDetailsResponse.toEntity(): ActorDetails {
    return ActorDetails(
        id = id,
        name = name,
        gender = gender,
        adult = adult,
        birthday = birthday,
        deathDay = deathDay,
        placeOfBirth = placeOfBirth,
        biography = biography,
        alsoKnownAs = alsoKnownAs,
        homePage = homePage,
        imdbId = imdbId,
        knownForDepartment = knownForDepartment,
        popularity = popularity,
        profilePath = profilePath.asImageUrlOrEmpty()
    )
}
