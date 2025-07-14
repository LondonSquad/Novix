package com.london.domain.entity.tvshowdetails

data class CastMemberEntity(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profilePath: String?,
    val character: String,
    val creditId: String,
    val order: Int
)
