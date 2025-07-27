package com.london.domain.entity.actordetails

import com.london.domain.KoverIgnore


@KoverIgnore
data class ActorDetails(
    val id: Int = 0,
    val name: String = "",
    val birthday: String = "",
    val deathDay: String? = null,
    val placeOfBirth: String = "",
    val biography: String = "",
    val knownForDepartment: String = "",
    val profileUrl: String =""
)
