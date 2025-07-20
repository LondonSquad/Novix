package com.london.data.datasource.remote.details.moviedetails.model.similarmovies

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimilarMoviesResponse(
    val page: Int?,
    @SerialName("results")
    val similarMovieRemotes: List<SimilarMovieRemote>?,
    @SerialName("total_pages")
    val totalPages: Int?,
    @SerialName("total_results")
    val totalResults: Int?
)