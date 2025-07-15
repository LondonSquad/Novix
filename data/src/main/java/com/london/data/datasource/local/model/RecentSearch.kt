package com.london.data.datasource.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_search_table")
data class RecentSearch(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val query: String,
    val date: Long
)
