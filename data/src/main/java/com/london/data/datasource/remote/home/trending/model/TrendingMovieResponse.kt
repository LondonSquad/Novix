package com.london.data.datasource.remote.home.trending.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendingMovieResponse(
    @SerialName("results")
    val results: List<TrendingMovieDto>,
    @SerialName("page")
    val page: Int,
    @SerialName("total_pages")
    val totalPages: Int
)

@Serializable
data class TrendingMovieDto(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val title: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("vote_average")
    val voteAverage: Double?,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = emptyList()
) 