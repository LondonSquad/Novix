package com.london.presentation.feature.home.popular

import com.london.domain.entity.shared.MediaType

data class PopularUiMedia(
    val id: Int,
    val name: String,
    val rating: String,
    val posterUrl: String,
    val mediaType: MediaType
)
