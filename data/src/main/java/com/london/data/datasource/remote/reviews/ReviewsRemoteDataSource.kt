package com.london.data.datasource.remote.reviews

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.reviews.model.ReviewResponse

interface ReviewsRemoteDataSource {
    suspend fun getMovieReviews(movieId: Int, pageNumber: Int): ApiResponse<ReviewResponse>
    suspend fun getTvShowReviews(tvShowId: Int, pageNumber: Int): ApiResponse<ReviewResponse>
}