package com.london.data.remote.model.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountInfoResponse(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("username")
    val userName: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("avatar")
    val avatar: AvatarInfo? = null
)

@Serializable
data class AvatarInfo(
    @SerialName("tmdb")
    val tmdb: AvatarDetails?
)

@Serializable
data class AvatarDetails(
    @SerialName("avatar_path")
    val avatarPath: String? = null
)
