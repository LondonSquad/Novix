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
