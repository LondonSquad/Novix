package com.london.data.datasource.remote.moviedetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieCastRemote(
    val id: Int,
    @SerialName("cast")
    val actorRemote: List<ActorRemote>,
    val crew: List<Crew>
)

@Serializable
data class ActorRemote(
    val adult: Boolean,
    val gender: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    val name: String,
    @SerialName("original_name")
    val originalName: String,
    val popularity: Double,
    @SerialName("profile_path")
    val profilePath: String? = null,
    @SerialName("cast_id")
    val castId: Int,
    val character: String,
    @SerialName("credit_id")
    val creditId: String,
    val order: Int
)

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