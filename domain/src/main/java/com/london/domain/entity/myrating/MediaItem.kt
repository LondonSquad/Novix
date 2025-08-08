package com.london.domain.entity.myrating

data class MediaItem(
    val id: Int,
    val posterPath: String,
    val title: String,
    val rating: Int,
    val isMovie: Boolean
) 