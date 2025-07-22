package com.london.data.datasource.remote.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Token(
    @SerialName("request_token")
    val requestToken: String
)