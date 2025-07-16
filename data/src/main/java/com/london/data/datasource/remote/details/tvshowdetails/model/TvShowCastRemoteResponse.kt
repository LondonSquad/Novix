package com.london.data.datasource.remote.details.tvshowdetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowCastRemoteResponse(
    @SerialName("cast")
    val cast: List<TvShowCastMember>,
    @SerialName("id")
    val id: Int? = null
)

@Serializable
data class TvShowCastMember(
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
    @SerialName("roles")
    val roles: List<Role>,
    @SerialName("total_episode_count")
    val totalEpisodeCount: Int,
    @SerialName("order")
    val order: Int
)

@Serializable
data class Role(
    @SerialName("credit_id")
    val creditId: String,
    @SerialName("character")
    val character: String,
    @SerialName("episode_count")
    val episodeCount: Int
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