package com.london.data.local.model.recent.viewed

@androidx.room.Entity(
    tableName = "recent_viewed_table",
    primaryKeys = ["id","type"]
)
data class RecentViewedLocal(
    val id : Int,
    val imageUrl : String,
    val type : com.london.data.local.model.recent.MediaTypeLocal,
    val viewDate : Long,
)