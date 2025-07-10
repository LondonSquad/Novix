package com.london.data.datasource.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_tv_shows_table")
data class SearchTvShowsResponseLocal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val query: String,
    val page: Int,
    val results: List<SearchTvShowsResponseDtoLocal>,
    val totalPages: Int,
    val totalResults: Int
)

data class SearchTvShowsResponseDtoLocal(
    val adult: Boolean,
    val backdropPath: String,
    val genreIds: List<Int>,
    val id: Int,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalName: String,
    val overview: String,
    val popularity: Int,
    val posterPath: String,
    val firstAirDate: String,
    val name: String,
    val voteAverage: Int,
    val voteCount: Int
)