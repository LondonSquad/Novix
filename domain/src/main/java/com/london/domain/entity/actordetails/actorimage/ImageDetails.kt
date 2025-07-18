package com.london.domain.entity.actordetails.actorimage

data class ImageDetails(
    val aspectRatio: Double = 0.0,
    val height: Int = 0,
    val iso: String? = null,
    val fileUrl: String = "",
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0,
    val width: Int = 0
)