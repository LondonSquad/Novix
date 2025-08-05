package com.london.data.remote.model.list

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCustomListBody(

    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
    @SerialName("language")
    val language: String,
)
