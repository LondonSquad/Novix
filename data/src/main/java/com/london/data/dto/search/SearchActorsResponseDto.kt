package com.london.data.dto.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchActorsResponse(
    @SerialName("page") val page: Int,
    @SerialName("results") val results: List<PersonDto>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

@Serializable
data class PersonDto(
    @SerialName ("adult") val adult: Boolean,
    @SerialName ("gender") val gender: Int,
    @SerialName ("id") val id: Int,
    @SerialName("known_for_department") val knownForDepartment: String? = null,
    @SerialName ("name") val name: String? = null,
    @SerialName("original_name") val originalName: String? = null,
    @SerialName ("popularity") val popularity: Double,
    @SerialName("profile_path") val profilePath: String? = null,
    @SerialName("known_for") val knownFor: List<KnownForDto>
)

@Serializable
data class KnownForDto(
    @SerialName("adult") val adult: Boolean,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String? = null,
    @SerialName("original_title") val originalTitle: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("media_type") val mediaType: String? = null,
    @SerialName("original_language") val originalLanguage: String? = null,
    @SerialName("genre_ids") val genreIds: List<Int>,
    @SerialName("popularity") val popularity: Double,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("video") val video: Boolean? = null,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,
)