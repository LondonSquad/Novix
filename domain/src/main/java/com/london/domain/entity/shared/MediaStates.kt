package com.london.domain.entity.shared

data class MediaStates(
    val favorite: Boolean,
    val id: Int,
    val rate: Int,
    val watchlist: Boolean
)