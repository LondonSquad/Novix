package com.london.domain.entity.shared

data class MediaStates(
    val id: Int,
    val favorite: Boolean,
    val rate: Int,
    val watchlist: Boolean
)
