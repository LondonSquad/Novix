package com.london.data.datasource.remote.auth.model

import kotlinx.serialization.SerialName

data class CreateSessionWithLoginRequest(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String,
    @SerialName("request_token")
    val requestToken: String
)