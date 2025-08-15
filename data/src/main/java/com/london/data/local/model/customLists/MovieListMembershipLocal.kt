package com.london.data.local.model.customLists

import androidx.room.Entity

@Entity(tableName = "movie_list_membership", primaryKeys = ["movieId", "listId"])
data class MovieListMembershipLocal(
    val movieId: Int,
    val listId: Int,
    val addedAt: Long = System.currentTimeMillis()
)
