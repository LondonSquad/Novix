package com.london.data.remote.model.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvatarInfo(
    @SerialName("tmdb")
    val tmdb: AvatarDetails?
)
