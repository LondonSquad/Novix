package com.london.domain.entity.videoprovider

data class TvShowVideo(
    val id: String,
    val iso31661: String,
    val iso6391: String,
    val videoUrl: String,
    val name: String,
    val official: Boolean,
    val publishedAt: String,
    val site: String,
    val size: Int,
    val type: String
)