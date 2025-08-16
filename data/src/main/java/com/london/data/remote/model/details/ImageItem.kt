package com.london.data.remote.model.details

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageItem(
    @SerialName("id")
    val id: Int? = null,
    @SerialName("file_path")
    val filePath: String? = null,
)