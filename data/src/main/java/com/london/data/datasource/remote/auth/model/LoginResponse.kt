package com.london.data.datasource.remote.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    @SerialName("success")
    val success: Boolean?,
    @SerialName("expires_at")
    val expiresAt: String?,
    @SerialName("request_token")
    val requestToken: String?
)
