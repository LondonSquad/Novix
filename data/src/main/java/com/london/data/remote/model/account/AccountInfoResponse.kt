package com.london.data.remote.model.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountInfoResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val userName: String?,
)
