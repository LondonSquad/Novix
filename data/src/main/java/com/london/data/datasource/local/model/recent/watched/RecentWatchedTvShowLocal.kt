package com.london.data.datasource.local.model.recent.watched

import androidx.room.Entity

@Entity(tableName = "recent_watched_tv_show_table")
data class RecentWatchedTvShowLocal(
    val id: Int,
    val name: String,
    val posterPictureUrl: String,
    val releaseYear: Int,
    val rating: Int,
    val genres: List<Int>
)
