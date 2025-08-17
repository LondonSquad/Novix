package com.london.data.remote.model.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestTokenResponse(
    @SerialName("success")
    val success: Boolean? = null,
    @SerialName("expires_at")
    val expiresAt: String? = null,
    @SerialName("request_token")
    val requestToken: String? = null
)