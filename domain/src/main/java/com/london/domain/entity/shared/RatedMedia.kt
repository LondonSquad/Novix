package com.london.domain.entity.shared

data class RatedMedia(
    val id: Int,
    val title: String,
    val posterPath: String,
    val rating: Int,
    val mediaType: MediaType
)
