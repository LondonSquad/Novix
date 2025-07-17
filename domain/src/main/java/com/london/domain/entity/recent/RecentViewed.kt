package com.london.domain.entity.recent

data class RecentViewed(
    val id: Int,
    val imageUrl: String,
    val type: MediaType,
    val viewDate: Long,
)
