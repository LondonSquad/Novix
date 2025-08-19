package com.london.domain.entity.shared

import com.london.domain.entity.recent.MediaType

data class RatedMedia(
    val id: Int,
    val title: String,
    val posterPath: String,
    val rating: Int,
    val mediaType: MediaType
) 