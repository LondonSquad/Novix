package com.london.domain.entity.videoprovider

data class MovieVideo(
    val id: String,
    val videoUrl: String,
    val name: String,
    val site: String,
    val official: Boolean,
)