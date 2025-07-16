package com.london.data.datasource.remote.details.moviedetails.model.moviecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Crew(
    val adult: Boolean = false,
    val gender: Int = 0,
    val id: Int = 0,
    @SerialName("known_for_department")
    val knownForDepartment: String = "",
    val name: String = "",
    @SerialName("original_name")
    val originalName: String = "",
    val popularity: Double = 0.0,
    @SerialName("profile_path")
    val profilePath: String? = null,
    @SerialName("credit_id")
    val creditId: String = "",
    val department: String = "",
    val job: String = ""
)