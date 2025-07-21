package com.london.data.datasource.remote.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("session_id")
    val sessionId: String
)
