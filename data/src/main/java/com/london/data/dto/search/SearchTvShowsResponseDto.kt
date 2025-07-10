package com.london.data.dto.search

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity(tableName = "search_tv_shows_table")
data class SearchTvShowsResponse(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @Transient
    val date: Long = System.currentTimeMillis(),
    @Transient
    val query: String = "",
    val page: Int,
    val results: List<SearchTvShowsResponseDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class SearchTvShowsResponseDto(
    @SerialName("adult") val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String,
    @SerialName("genre_ids") val genreIds: List<Int>,
    @SerialName("id") val id: Int,
    @SerialName("origin_country") val originCountry: List<String>,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("original_name") val originalName: String,
    @SerialName("overview") val overview: String,
    @SerialName("popularity") val popularity: Int,
    @SerialName("poster_path") val posterPath: String,
    @SerialName("first_air_date") val firstAirDate: String,
    @SerialName("name") val name: String,
    @SerialName("vote_average") val voteAverage: Int,
    @SerialName("vote_count") val voteCount: Int
)