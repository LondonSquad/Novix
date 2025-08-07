package com.london.data.remote.model.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomListResponse(

    @SerialName("status_message")
    val statusMessage: String?,
    @SerialName("status_code")
    val statusCode: Int?
)
