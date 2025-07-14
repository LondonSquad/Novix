package com.london.domain.entity.tvshowdetails

data class ImageItemEntity(
    val aspectRatio: Double,
    val height: Int,
    val iso6391: String?,
    val filePath: String,
    val voteAverage: Double,
    val voteCount: Int,
    val width: Int
)
