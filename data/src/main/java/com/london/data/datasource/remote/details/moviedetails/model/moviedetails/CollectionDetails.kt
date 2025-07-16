package com.london.data.datasource.remote.details.moviedetails.model.moviedetails
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionDetails(
    val id: Int,
    val name: String
)
