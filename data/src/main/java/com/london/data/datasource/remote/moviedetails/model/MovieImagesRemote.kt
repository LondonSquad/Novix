package com.london.data.datasource.remote.moviedetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieImages(
    val backdrops: List<Backdrop>,
    val id: Int,
    val logos: List<Logo>,
    val posters: List<Poster>
)

@Serializable
data class Logo(
    @SerialName("aspect_ratio")
    val aspectRatio: Double,
    @SerialName("file_path")
    val filePath: String,
    val height: Int,
    @SerialName("iso_639_1")
    val iso6391: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    val width: Int
)

@Serializable
data class Backdrop(
    @SerialName("aspect_ratio")
    val aspectRatio: Double,
    val height: Int,
    @SerialName("iso_639_1")
    val iso6391: String?,
    @SerialName("file_path")
    val filePath: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    val width: Int
)

@Serializable
data class Poster(
    @SerialName("aspect_ratio")
    val aspectRatio: Double,
    @SerialName("file_path")
    val filePath: String,
    val height: Int,
    @SerialName("iso_639_1")
    val iso6391: String?,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int,
    val width: Int
)