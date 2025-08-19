package com.london.presentation.feature.home

import com.london.domain.entity.shared.MediaType

data class HomeUiMedia(
    val id: Int,
    val posterUrl: String,
    val mediaType: MediaType
)
