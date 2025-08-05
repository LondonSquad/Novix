package com.london.data.remote.model.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCustomListResponse(

    @SerialName("list_id")
    val id: Int,
    @SerialName("status_message")
    val statusMessage: String,
    @SerialName("success")
    val success: Boolean,
    @SerialName("status_code")
    val statusCode: Int,
)
