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
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdrop_path: String?,
    @SerialName("genre_ids")
    val genre_ids: List<Int>,
    @SerialName("id")
    val id: Int,
    @SerialName("original_language")
    val original_language: String,
    @SerialName("original_title")
    val original_title: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("popularity")
    val popularity: Int,
    @SerialName("poster_path")
    val poster_path: String?,
    @SerialName("release_date")
    val release_date: String,
    @SerialName("title")
    val title: String,
    @SerialName("video")
    val video: Boolean,
    @SerialName("vote_average")
    val vote_average: Int,
    @SerialName("vote_count")
    val vote_count: Int
)