package com.london.data.mapper.search

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.reviews.AuthorDetailsResponse
import com.london.data.remote.model.reviews.ReviewResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orDefault
import com.london.data.utils.orZero
import com.london.domain.entity.review.AuthorDetails
import com.london.domain.entity.review.Review
import com.london.domain.entity.shared.PagedFetchResponse

fun ApiResponse<ReviewResponse>.toReviewEntity(): PagedFetchResponse<Review> =
    PagedFetchResponse(
        items = items.map { it.toReviewEntity() },
        currentPage = currentPage,
        totalPages = totalPages.orDefault(),
        totalItems = totalItems
    )

fun ReviewResponse.toReviewEntity(): Review =
    Review(
        authorName = author.orEmpty(),
        authorDetails = authorDetailsResponse.toAuthorDetails(),
        content = content.orEmpty(),
        createdAt = createdAt.orEmpty(),
        id = id.orEmpty(),
    )

fun AuthorDetailsResponse.toAuthorDetails(): AuthorDetails =
    AuthorDetails(
        name = authorName.orEmpty(),
        username = authorUsername.orEmpty(),
        profileUrl = authorPictureUrl.asImageUrlOrEmpty(),
        rating = rating.orZero()
    )
