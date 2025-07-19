package com.london.domain.entity.recent

data class RecentSearch(
    val id: Int,
    val query: String,
    val timestamp: Long
)
