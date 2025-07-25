package com.london.data.datasource.remote.home.trending.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingResponse(
    @SerialName("results")
    val results: List<TrendingDto>,
    @SerialName("page")
    val page: Int,
    @SerialName("total_pages")
    val totalPages: Int
)

@Serializable
data class TrendingDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("profile_path")
    val profilePath: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = emptyList()
) {
    val image: String?
        get() = posterPath ?: profilePath
} 