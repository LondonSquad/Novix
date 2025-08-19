package com.london.data.remote.model.details.tvshow.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowCastRemoteResponse(
    @SerialName("cast")
    val cast: List<TvShowCastMemberResponse>? = null,
    @SerialName("id")
    val id: Int? = null
)

@Serializable
data class TvShowCastMemberResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("profile_path")
    val profilePath: String? = null,
    @SerialName("roles")
    val roles: List<Role>? = null,
)

@Serializable
data class Role(
    @SerialName("character")
    val character: String? = null,
    @SerialName("episode_count")
    val episodeCount: Int? = null,
)
