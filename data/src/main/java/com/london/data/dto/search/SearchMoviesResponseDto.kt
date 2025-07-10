package com.london.data.dto.search

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity(tableName = "search_movies_table")
data class SearchMoviesResponse(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @Transient
    val date: Long = System.currentTimeMillis(),
    @Transient
    val query: String = "",
    val page: Int,
    val results: List<SearchMoviesResponseDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class SearchMoviesResponseDto(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    @SerialName("id")
    val id: Int,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("popularity")
    val popularity: Int,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("title")
    val title: String,
    @SerialName("video")
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Int,
    @SerialName("vote_count")
    val voteCount: Int
)