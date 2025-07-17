package com.london.data.datasource.remote.details.moviedetails.model.moviecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieActor(
    val adult: Boolean?,
    val gender: Int?,
    @SerialName("id")
    val id: Int?,
    @SerialName("known_for_department")
    val knownForDepartment: String?,
    val name: String?,
    @SerialName("original_name")
    val originalName: String?,
    val popularity: Double?,
    @SerialName("profile_path")
    val profilePath: String?,
    @SerialName("cast_id")
    val castId: Int?,
    val character: String?,
    @SerialName("credit_id")
    val creditId: String?,
    val order: Int?,
)