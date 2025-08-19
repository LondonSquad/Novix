package com.london.domain.entity.review

import com.london.domain.KoverIgnore

@KoverIgnore
data class AuthorDetails(
    val name: String,
    val username: String,
    val profileUrl: String,
    val rating: Double
)