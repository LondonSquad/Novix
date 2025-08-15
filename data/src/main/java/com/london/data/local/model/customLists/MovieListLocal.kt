package com.london.data.local.model.customLists

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_movie_lists")
data class MovieListLocal(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String?,
    val itemCount: Int,
    val cachedAt: Long = System.currentTimeMillis()
)
