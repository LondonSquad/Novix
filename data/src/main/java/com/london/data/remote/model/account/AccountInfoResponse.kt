package com.london.data.remote.model.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountInfoResponse(
    @SerialName("id")
    val id: Int?,
    @SerialName("username")
    val userName: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("avatar")
    val avatar: AvatarInfo?,
    @SerialName("avatar_path")
    val avatarPath: String?
)

@Serializable
data class AvatarInfo(
    @SerialName("tmdb")
    val tmdb: AvatarDetails?
)

@Serializable
data class AvatarDetails(
    @SerialName("avatar_path")
    val avatarPath: String?
)
