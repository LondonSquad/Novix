package com.london.data.remote.model.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteSessionResponse(
    @SerialName("success")
    val success: Boolean?
)
