package com.london.domain.entity.actordetails

data class ActorImageDetails(
    val id: Int,
    val profiles: List<ImageDetails>
)

data class ImageDetails(
    val aspectRatio: Double,
    val height: Int,
    val iso: String?,
    val filePath: String,
    val voteAverage: Double,
    val voteCount: Int,
    val width: Int
)