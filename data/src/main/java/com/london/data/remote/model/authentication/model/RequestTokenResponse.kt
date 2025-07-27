package com.london.data.remote.model.authentication.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestTokenResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("expires_at")
    val expiresAt: String,
    @SerialName("request_token")
    val requestToken: String
)