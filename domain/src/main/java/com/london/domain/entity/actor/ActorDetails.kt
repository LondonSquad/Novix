package com.london.domain.entity.actor

data class ActorDetails(
    val id: Int = 0,
    val name: String = "",
    val birthday: String = "",
    val biography: String = "",
    val profileUrl: String = "",
    val deathDay: String? = null,
    val placeOfBirth: String = "",
    val knownForDepartment: String = ""
)
