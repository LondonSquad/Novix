package com.london.data.dto.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchTvShowsResponse(
    val page: Int,
    val results: List<SearchTvShowsResponseDto>,
    val total_pages: Int,
    val total_results: Int
)

@Serializable
data class SearchTvShowsResponseDto(
    @SerialName("adult") val adult: Boolean,
    @SerialName("backdrop_path") val backdrop_path: String,
    @SerialName("genre_ids") val genre_ids: List<Int>,
    @SerialName("id") val id: Int,
    @SerialName("origin_country") val origin_country: List<String>,
    @SerialName("original_language") val original_language: String,
    @SerialName("original_name") val original_name: String,
    @SerialName("overview") val overview: String,
    @SerialName("popularity") val popularity: Int,
    @SerialName("poster_path") val poster_path: String,
    @SerialName("first_air_date") val first_air_date: String,
    @SerialName("name") val name: String,
    @SerialName("vote_average") val vote_average: Int,
    @SerialName("vote_count") val vote_count: Int
    )