package com.london.data.local.model.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_actors_table")
data class SearchActorsLocal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val query: String,
    val page: Int,
    val results: List<ActorLocal>,
    val totalPages: Int,
    val totalResults: Int
)

data class ActorLocal(
    val id: Int,
    val name: String,
    val profilePicture: String,
)