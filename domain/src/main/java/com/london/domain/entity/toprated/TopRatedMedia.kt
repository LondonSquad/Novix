package com.london.domain.entity.toprated

import com.london.domain.entity.genre.Genre
import com.london.domain.entity.shared.MediaType

data class TopRatedMedia(
    val id: Int,
    val posterUrl: String,
    val name: String,
    val genres: List<Genre>,
    val mediaType: MediaType
)
