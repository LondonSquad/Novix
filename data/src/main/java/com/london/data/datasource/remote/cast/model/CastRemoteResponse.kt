package com.london.data.datasource.remote.cast.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CastRemoteResponse(
    @SerialName("cast")
    val cast: List<CastMember>,
    @SerialName("crew")
    val crew: List<CrewMember>,
    @SerialName("id")
    val id: Int
)

@Serializable
data class CastMember(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("gender")
    val gender: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    @SerialName("name")
    val name: String,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("profile_path")
    val profilePath: String?,
    @SerialName("character")
    val character: String,
    @SerialName("credit_id")
    val creditId: String,
    @SerialName("order")
    val order: Int
)

@Serializable
data class CrewMember(
    @SerialName("adult")
    val adult: Boolean,
    @SerialName("gender")
    val gender: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("known_for_department")
    val knownForDepartment: String,
    @SerialName("name")
    val name: String,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("profile_path")
    val profilePath: String?,
    @SerialName("credit_id")
    val creditId: String,
    @SerialName("department")
    val department: String,
    @SerialName("job")
    val job: String
)