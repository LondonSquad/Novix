package com.london.domain.entity.tvshowdetails

data class TvShowCreatorEntity(
    val id: Int,
    val creditId: String,
    val name: String,
    val originalName: String,
    val gender: Int,
    val profilePath: String?
)
