package com.london.data.remote.model.reviews

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    @SerialName("author")
    val author: String?,
    @SerialName("author_details")
    val authorDetailsResponse: AuthorDetailsResponse,
    @SerialName("content")
    val content: String?,
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("updated_at")
    val updatedAt: String?,
    @SerialName("url")
    val url: String?
)

@Serializable
data class AuthorDetailsResponse(
    @SerialName("name")
    val authorName: String?,
    @SerialName("username")
    val authorUsername: String?,
    @SerialName("avatar_path")
    val authorPictureUrl: String?,
    @SerialName("rating")
    val rating: Double?
)