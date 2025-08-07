package com.london.data.remote.model.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountInfoResponse(
    @SerialName("id")
    val id: Int?,
    @SerialName("username")
    val userName: String?,
    @SerialName("name")
    val name: String?
)
