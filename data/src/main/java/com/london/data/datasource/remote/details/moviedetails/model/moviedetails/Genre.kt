package com.london.data.datasource.remote.details.moviedetails.model.moviedetails

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: Int,
    val name: String
)