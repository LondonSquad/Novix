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
    @SerialName("backdrop_path") val backdrop_path: String? = null,
    @SerialName("genre_ids") val genre_ids: List<Int>,
    @SerialName("id") val id: Int,
    @SerialName("origin_country") val origin_country: List<String>,
    @SerialName("original_language") val original_language: String? = null,
    @SerialName("original_name") val original_name: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("popularity") val popularity: Double,
    @SerialName("poster_path") val poster_path: String? = null,
    @SerialName("first_air_date") val first_air_date: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("vote_average") val vote_average: Double,
    @SerialName("vote_count") val vote_count: Int
    )