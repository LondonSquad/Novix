package com.london.data.remote.model.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginValidationRequestBody(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
    @SerialName("request_token")
    val requestToken: String
)