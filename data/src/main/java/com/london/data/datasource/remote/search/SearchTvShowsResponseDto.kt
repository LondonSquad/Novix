package com.london.data.datasource.remote.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchTvShowsResponse(
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