package com.london.data.remote.model.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GuestSessionResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("guest_session_id")
    val guestSessionId: String,
    @SerialName("expires_at")
    val expiresAt: String?
)