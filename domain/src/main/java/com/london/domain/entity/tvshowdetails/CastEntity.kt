package com.london.domain.entity.tvshowdetails

data class CastEntity(
    val cast: List<CastMemberEntity>,
    val crew: List<CrewMemberEntity>,
    val id: Int
)

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

data class CrewMemberEntity(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profilePath: String?,
    val creditId: String,
    val department: String,
    val job: String
)