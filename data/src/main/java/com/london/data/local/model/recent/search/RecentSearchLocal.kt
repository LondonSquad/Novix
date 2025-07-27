package com.london.data.local.model.recent.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_search_table")
data class RecentSearchLocal(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val query: String,
    val date: Long
)