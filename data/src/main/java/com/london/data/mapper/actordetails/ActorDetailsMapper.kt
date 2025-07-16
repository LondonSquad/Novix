package com.london.data.mapper.actordetails

import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsResponse
import com.london.domain.entity.actordetails.ActorDetails

fun ActorDetailsResponse.toEntity():ActorDetails{
    return ActorDetails(
        id = this.id,
        name = this.name,
        gender = this.gender,
        adult = this.adult,
        birthday = this.birthday,
        deathDay = this.deathDay,
        placeOfBirth = this.placeOfBirth,
        biography = this.biography,
        alsoKnownAs = this.alsoKnownAs,
        homePage = this.homePage,
        imdbId = this.imdbId,
        knownForDepartment = this.knownForDepartment,
        popularity = this.popularity,
        profilePath = this.profilePath
    )
}


