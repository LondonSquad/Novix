package com.london.domain.entity.actordetails

import com.london.domain.KoverIgnore

@KoverIgnore
data class ActorDetails(
    val id: Int = 0,
    val name: String = "",
    val birthday: String = "",
    val deathDay: String? = null,
    val biography: String = "",
    val profileUrl: String = "",
    val placeOfBirth: String = "",
    val knownForDepartment: String = ""
)
