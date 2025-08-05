package com.london.data.remote.model.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomListResponse(

    @SerialName("status_code")
    val statusMessage: String,
    @SerialName("status_message")
    val statusCode: Int
)
