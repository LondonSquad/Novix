package com.london.data.remote.model.myrating

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RatedTvShowResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String,
    @SerialName("genre_ids")
    val genreIds: List<Int>? = null,
    @SerialName("origin_country")
    val originCountry: List<String>? = null,
    @SerialName("original_language")
    val originalLanguage: String? = null,
    @SerialName("original_name")
    val originalName: String? = null,
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("popularity")
    val popularity: Double? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName ("name")
    val name:String?= null,
    @SerialName("vote_average")
    val voteAverage: Double?,
    @SerialName("rating")
    val rating: Double? = null
)