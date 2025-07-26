package com.london.data.mapper

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.reviews.model.AuthorDetailsResponse
import com.london.data.remote.model.reviews.model.ReviewResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.review.AuthorDetails
import com.london.domain.entity.review.ReviewEntity

fun ApiResponse<ReviewResponse>.toReviewEntity(): PagedFetchResponse<ReviewEntity> =
    PagedFetchResponse(
        items = this.items.map { it.toReviewEntity() },
        currentPage = this.currentPage,
        totalPages = if (totalPages!= 0) this.totalPages else 1,
        totalItems = this.totalItems
    )

fun ReviewResponse.toReviewEntity(): ReviewEntity =
    ReviewEntity(
        authorName = this.author.orEmpty(),
        authorDetails = authorDetailsResponse.toAuthorDetails(),
        content = content.orEmpty(),
        createdAt = createdAt.orEmpty(),
        id = id.orEmpty(),
        updatedAt = updatedAt.orEmpty(),
        url = url.asImageUrlOrEmpty()
    )

fun AuthorDetailsResponse.toAuthorDetails(): AuthorDetails =
    AuthorDetails(
        name = this.authorName.orEmpty(),
        username = this.authorUsername.orEmpty(),
        profileUrl = authorPictureUrl.asImageUrlOrEmpty(),
        rating = this.rating.orZero()
    )