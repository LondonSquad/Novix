package com.london.data.local.model.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_actors_table")
data class SearchActorsLocal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long = System.currentTimeMillis(),
    val query: String,
    val page: Int,
    val results: List<PersonDtoLocal>,
    val totalPages: Int,
    val totalResults: Int
)

data class PersonDtoLocal(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String,
    val name: String,
    val originalName: String,
    val popularity: Double,
    val profileUrl: String?,
    val knownFor: List<KnownForDtoLocal>
)

data class KnownForDtoLocal(
    val adult: Boolean,
    val backdropPath: String?,
    val id: Int,
    val title: String? = null,
    val originalTitle: String? = null,
    val overview: String? = null,
    val posterUrl: String? = null,
    val mediaType: String,
    val originalLanguage: String,
    val genreIds: List<Int>,
    val popularity: Double,
    val releaseDate: String? = null,
    val video: Boolean? = null,
    val voteAverage: Double,
    val voteCount: Int,
    val name: String? = null,
    val originalName: String? = null,
    val firstAirDate: String? = null,
    val originCountry: List<String>? = null
)
