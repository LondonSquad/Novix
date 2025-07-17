package com.london.data.datasource.local.model

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
    val adult: Boolean,
    val backdropUrl: String,
    val genreIds: List<Int>,
    val id: Int,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val firstAirDate: String,
    val name: String,
    val voteAverage: Double,
    val voteCount: Int
)