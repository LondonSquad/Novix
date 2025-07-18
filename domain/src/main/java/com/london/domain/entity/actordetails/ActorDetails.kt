package com.london.domain.entity.actordetails

import com.london.domain.KoverIgnore


@KoverIgnore
data class ActorDetails(
    val id: Int = 0,
    val name: String = "",
    val gender: Int = 0,
    val adult: Boolean = false,
    val birthday: String = "",
    val deathDay: String? = null,
    val placeOfBirth: String = "",
    val biography: String = "",
    val alsoKnownAs: List<String> = listOf(),
    val homePage: String? = null,
    val imdbId: String = "",
    val knownForDepartment: String = "",
    val popularity: Double = 0.0,
    val profileUrl: String =""
)
