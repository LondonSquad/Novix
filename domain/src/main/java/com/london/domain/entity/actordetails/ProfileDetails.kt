package com.london.domain.entity.actordetails

data class ProfilesResponseDetails(
    val id: Int,
    val profiles: List<ProfileDetails>
)

data class ProfileDetails(
    val aspectRatio: Double,
    val height: Int,
    val iso: String?,
    val filePath: String,
    val voteAverage: Double,
    val voteCount: Int,
    val width: Int
)