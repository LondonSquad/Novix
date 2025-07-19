package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.actordetails.ActorDetails

@KoverIgnore
fun ActorDetailsResponse.toEntity():ActorDetails{
    return ActorDetails(
        id = this.id.orZero(),
        name = this.name.orEmpty(),
        gender = this.gender.orZero(),
        adult = this.adult,
        birthday = this.birthday.orEmpty(),
        deathDay = this.deathDay,
        placeOfBirth = this.placeOfBirth.orEmpty(),
        biography = this.biography.orEmpty(),
        alsoKnownAs = this.alsoKnownAs,
        homePage = this.homePage,
        imdbId = this.imdbId.orEmpty(),
        knownForDepartment = this.knownForDepartment.orEmpty(),
        popularity = this.popularity.orZero(),
        profileUrl = profilePath.asImageUrlOrEmpty()
    )
}


