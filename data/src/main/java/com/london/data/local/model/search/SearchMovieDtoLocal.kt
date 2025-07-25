package com.london.data.local.model.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_movies_table")
data class SearchMoviesLocal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val query: String,
    val page: Int,
    val results: List<SearchMovieDtoLocal>,
    val totalPages: Int,
    val totalResults: Int
)

data class SearchMovieDtoLocal(
    val adult: Boolean,
    val backdropPath: String?,
    val genreIds: List<Int>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)