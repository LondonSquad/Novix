package com.london.domain.entity.review

import com.london.domain.KoverIgnore

@KoverIgnore
data class ReviewEntity(
    val authorName: String,
    val authorDetails: AuthorDetails,
    val content: String,
    val createdAt: String,
    val id: String,
    val updatedAt: String,
    val url: String
)

@KoverIgnore
data class AuthorDetails(
    val name: String,
    val username: String,
    val profileUrl: String,
    val rating: Double
)