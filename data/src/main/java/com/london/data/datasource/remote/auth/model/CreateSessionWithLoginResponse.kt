package com.london.data.datasource.remote.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionWithLoginResponse(
    @SerialName("success")
    val success: Boolean?,
    @SerialName("expires_at")
    val expiresAt: String?
)



