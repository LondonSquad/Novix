package com.london.domain.entity

data class TvShowImagesEntity(
    val backdrops: List<ImageItemEntity>,
    val id: Int,
    val logos: List<ImageItemEntity>,
    val posters: List<ImageItemEntity>
)

data class ImageItemEntity(
    val aspectRatio: Double,
    val height: Int,
    val iso6391: String?,
    val filePath: String,
    val voteAverage: Double,
    val voteCount: Int,
    val width: Int
)