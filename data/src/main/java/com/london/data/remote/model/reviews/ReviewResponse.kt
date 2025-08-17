package com.london.data.remote.model.reviews

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    @SerialName("author")
    val author: String? = null,
    @SerialName("author_details")
    val authorDetailsResponse: AuthorDetailsResponse,
    @SerialName("content")
    val content: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("id")
    val id: String? = null,
)

@Serializable
data class AuthorDetailsResponse(
    @SerialName("name")
    val authorName: String? = null,
    @SerialName("username")
    val authorUsername: String? = null,
    @SerialName("avatar_path")
    val authorPictureUrl: String? = null,
    @SerialName("rating")
    val rating: Double? = null
)