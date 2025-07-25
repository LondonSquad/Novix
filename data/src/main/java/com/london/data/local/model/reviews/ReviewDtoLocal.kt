package com.london.data.local.model.reviews

data class ReviewsLocal(
    val id: Int,
    val page: Int,
    val results: List<ReviewDto>,
    val totalPages: Int,
    val totalResults: Int
)

data class ReviewDto(
    val author: String,
    val authorDetails: AuthorDetailsDto,
    val content: String,
    val createdAt: String,
    val id: String,
    val updatedAt: String,
    val url: String
)

data class AuthorDetailsDto(
    val name: String,
    val username: String,
    val profileUrl: String,
    val rating: Int
)