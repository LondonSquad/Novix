package com.london.domain.entity

import com.london.domain.entity.recent.MediaType

data class RatedMedia(
    val id: Int,
    val title: String,
    val posterPath: String,
    val rating: Int,
    val mediaType: MediaType
) 