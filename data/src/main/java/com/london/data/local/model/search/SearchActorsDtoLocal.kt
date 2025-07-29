package com.london.data.local.model.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_actors_table")
data class SearchActorsLocal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val query: String,
    val page: Int,
    val results: List<ActorLocal>,
    val totalPages: Int,
    val totalResults: Int
)

data class ActorLocal(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profileUrl: String,
    val knownFor: List<KnownForLocal>
)

data class KnownForLocal(
    val adult: Boolean,
    val backdropPath: String,
    val id: Int,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterUrl: String,
    val mediaType: String,
    val originalLanguage: String,
    val genreIds: List<Int>,
    val popularity: Double,
    val releaseDate: String,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int,
    val name: String,
    val originalName: String,
    val firstAirDate: String,
    val originCountry: List<String>
)
