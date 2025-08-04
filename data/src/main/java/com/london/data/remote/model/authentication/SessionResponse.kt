package com.london.data.remote.model.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("session_id")
    val sessionId: String
)
