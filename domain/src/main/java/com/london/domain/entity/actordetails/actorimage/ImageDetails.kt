package com.london.domain.entity.actordetails.actorimage

data class ImageDetails(
    val aspectRatio: Double,
    val height: Int,
    val iso: String?,
    val fileUrl: String,
    val voteAverage: Double,
    val voteCount: Int,
    val width: Int
)