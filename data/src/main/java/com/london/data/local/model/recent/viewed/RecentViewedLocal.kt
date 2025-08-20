package com.london.data.local.model.recent.viewed

import com.london.data.local.model.recent.MediaTypeLocal

@androidx.room.Entity(
    tableName = "recent_viewed_table",
    primaryKeys = ["id", "type"]
)
data class RecentViewedLocal(
    val id: Int,
    val imageUrl: String,
    val type: MediaTypeLocal,
    val viewDate: Long,
)
