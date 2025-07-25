package com.london.data.remote.source.reviews

import com.london.data.datasource.remote.ApiResponse
import com.london.data.remote.model.reviews.model.ReviewResponse

interface ReviewsRemoteDataSource {
    suspend fun getMovieReviews(movieId: Int, pageNumber: Int): Result<ApiResponse<ReviewResponse>>
    suspend fun getTvShowReviews(
        tvShowId: Int,
        pageNumber: Int
    ): Result<ApiResponse<ReviewResponse>>
}