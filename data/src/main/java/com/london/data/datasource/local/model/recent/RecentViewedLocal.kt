package com.london.data.datasource.local.model.recent

import androidx.room.Entity

@Entity(
    tableName = "recent_viewed_table",
    primaryKeys = ["id","type"]
)
data class RecentViewedLocal(
    val id : Int,
    val imageUrl : String,
    val type : MediaType,
    val viewDate : Long,
)
