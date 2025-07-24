package com.london.data.datasource.local.model.recent.watched

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_watched_movie_table")
data class RecentWatchedMovieLocal(
    @PrimaryKey
    val id: Int,
    val name: String,
    val posterPictureUrl: String,
    val releaseYear: Int,
    val rating: Int,
    val genreIds: List<Int>,
    val watchedAt: Long
)
