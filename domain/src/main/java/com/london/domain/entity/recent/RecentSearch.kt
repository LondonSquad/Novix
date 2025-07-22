package com.london.domain.entity.recent

import com.london.domain.KoverIgnore

@KoverIgnore
data class RecentSearch(
    val id: Int,
    val query: String,
    val timestamp: Long
)
