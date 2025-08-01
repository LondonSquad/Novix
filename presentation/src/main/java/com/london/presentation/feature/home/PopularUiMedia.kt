package com.london.presentation.feature.home

import com.london.domain.entity.recent.MediaType

data class PopularUiMedia(
    val name: String,
    val rating: String,
    val id: Int,
    val posterUrl: String,
    val mediaType: MediaType
)