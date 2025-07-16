package com.london.domain.entity.tvshowdetails.episode

data class EpisodeCrewMemberEntity(
    val job: String,
    val department: String,
    val creditId: String,
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profilePath: String?
)
