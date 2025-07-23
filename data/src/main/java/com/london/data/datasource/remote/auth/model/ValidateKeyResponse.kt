package com.london.data.datasource.remote.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ValidateKeyResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("status_code")
    val expiresAt: String,
    @SerialName("status_message")
    val requestToken: String

)