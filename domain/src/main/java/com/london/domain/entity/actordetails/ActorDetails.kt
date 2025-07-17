package com.london.domain.entity.actordetails

import com.london.domain.KoverIgnore


@KoverIgnore
data class ActorDetails(
    val id: Int,
    val name: String,
    val gender: Int,
    val adult: Boolean,
    val birthday: String,
    val deathDay: String? = null,
    val placeOfBirth: String,
    val biography: String,
    val alsoKnownAs: List<String>,
    val homePage: String? = null,
    val imdbId: String,
    val knownForDepartment: String,
    val popularity: Double,
    val profilePath: String
)
