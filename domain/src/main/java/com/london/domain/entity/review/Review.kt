package com.london.domain.entity.review

data class Review(
    val authorName: String,
    val authorDetails: AuthorDetails,
    val content: String,
    val createdAt: String,
    val id: String,
)
