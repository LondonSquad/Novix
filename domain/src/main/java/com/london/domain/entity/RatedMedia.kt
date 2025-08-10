package com.london.domain.entity

data class RatedMedia(
    val id: Int,
    val title: String,
    val posterPath: String,
    val rating: Int,
    val isMovie: Boolean
) 