package com.london.data.local.model.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_tv_shows_table")
data class SearchTvShowLocal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val query: String,
    val page: Int,
    val results: List<SearchTvShowDtoLocal>,
    val totalPages: Int,
    val totalResults: Int
)

data class SearchTvShowDtoLocal(
    val backdropUrl: String,
    val genreIds: List<Int>,
    val id: Int,
    val posterPath: String,
    val firstAirDate: String,
    val name: String,
    val voteAverage: Double,
)
