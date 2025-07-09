package com.london.data.dto.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchMoviesResponse(
    val page: Int,
    val results: List<SearchMoviesResponseDto>,
    val total_pages: Int,
    val total_results: Int
)

@Serializable
data class SearchMoviesResponseDto(
    @SerialName("adult") val adult: Boolean,
    @SerialName("backdrop_path") val backdrop_path: String? = null,
    @SerialName("genre_ids") val genre_ids: List<Int>,
    @SerialName("id") val id: Int,
    @SerialName("original_language") val original_language: String? = null,
    @SerialName("original_title") val original_title: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("popularity") val popularity: Float,
    @SerialName("poster_path") val poster_path: String? = null,
    @SerialName("release_date") val release_date: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("video") val video: Boolean,
    @SerialName("vote_average") val vote_average: Float,
    @SerialName("vote_count") val vote_count: Int
)