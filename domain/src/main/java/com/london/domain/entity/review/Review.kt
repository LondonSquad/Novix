package com.london.domain.entity.review

import com.london.domain.KoverIgnore

@KoverIgnore
data class Review(
    val authorName: String,
    val authorDetails: AuthorDetails,
    val content: String,
    val createdAt: String,
    val id: String,
)
