package com.london.data.datasource.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_movies_table")
data class SearchMoviesResponseLocal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val query: String,
    val page: Int,
    val results: List<SearchMoviesResponseDtoLocal>,
    val totalPages: Int,
    val totalResults: Int
)

data class SearchMoviesResponseDtoLocal(
    val adult: Boolean,
    val backdropPath: String?,
    val genreIds: List<Int>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Int,
    val posterPath: String?,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Int,
    val voteCount: Int
)