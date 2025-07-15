package com.london.domain.entity.tvshowdetails.episode

data class EpisodeGuestStarEntity(
    val character: String,
    val creditId: String,
    val order: Int,
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profilePath: String?
)
