package com.london.presentation.feature.list.viewlistitems.uistate

import com.london.domain.entity.recent.MediaType

data class MediaUi(
    val id: UInt,
    val posterUrl: String,
    val mediaType: MediaType
)