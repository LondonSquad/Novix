package com.london.data.datasource.local.model.recent

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_search_table")
data class RecentSearchLocal(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val query: String,
    val date: Long
)