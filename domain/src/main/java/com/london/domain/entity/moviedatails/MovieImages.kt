package com.london.domain.entity.moviedatails

data class MovieImages(
    val backdrops: List<String>,
    val id: Int,
    val logos: List<String>,
    val posters: List<String>
)