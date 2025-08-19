package com.london.data.remote.model.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvatarDetails(
    @SerialName("avatar_path")
    val avatarPath: String? = null
)
