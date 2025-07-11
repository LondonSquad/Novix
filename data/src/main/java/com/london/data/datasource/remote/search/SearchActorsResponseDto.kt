package com.london.data.datasource.remote.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchActorsResponse(
    val page: Int,
    val results: List<PersonDto>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
)

@Serializable
data class PersonDto(
    val adult: Boolean,
    val gender: Int,
    val id: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    val name: String,
    @SerialName("original_name")
    val originalName: String,
    val popularity: Double,
    @SerialName("profile_path")
    val profilePath: String?,
    @SerialName("known_for")
    val knownFor: List<KnownForDto>
)

@Serializable
data class KnownForDto(
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    val id: Int,
    val title: String? = null,
    @SerialName("original_title")
    val originalTitle: String? = null,
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("media_type")
    val mediaType: String,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("genre_ids")
    val genreIds: List<Int>,
    val popularity: Double,
    @SerialName("release_date")
    val releaseDate: String? = null,
    val video: Boolean? = null,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    val name: String? = null,
    @SerialName("original_name")
    val originalName: String? = null,
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName("origin_country")
    val originCountry: List<String>? = null
)