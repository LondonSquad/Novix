package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.ActorDetails

@KoverIgnore
fun ActorDetailsResponse.toEntity(): ActorDetails {
    return ActorDetails(
        id = id.orZero(),
        name = name.orEmpty(),
        gender = gender.orZero(),
        adult = adult.isTrue,
        birthday = birthday.orEmpty(),
        deathDay = deathDay,
        placeOfBirth = placeOfBirth.orEmpty(),
        biography = biography.orEmpty(),
        alsoKnownAs = alsoKnownAs.orEmpty(),
        homePage = homePage,
        imdbId = imdbId.orEmpty(),
        knownForDepartment = knownForDepartment.orEmpty(),
        popularity = popularity.orZero(),
        profileUrl = profilePath.asImageUrlOrEmpty()
    )
}


