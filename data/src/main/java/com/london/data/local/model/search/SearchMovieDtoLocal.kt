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
    val id: Int,
    val name: String,
    val posterPath: String,
    val releaseYear: Int,
    val rating: Int,
    val genreIds: List<Int>,
)
