package com.london.domain.entity.recent

data class RecentSearch(
    val id: Int = 0,
    val query: String,
    val timestamp: Long = System.currentTimeMillis()
)
