package com.london.data.datasource.remote.reviews

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.reviews.api.ReviewsApiService
import com.london.data.datasource.remote.reviews.model.ReviewResponse
import org.koin.core.annotation.Single

@Single
class ReviewsRemoteDataSourceImpl(
    private val reviewsApiService: ReviewsApiService
) : ReviewsRemoteDataSource {
    override suspend fun getMovieReviews(
        movieId: Int,
        pageNumber: Int
    ): ApiResponse<ReviewResponse> =
        reviewsApiService.getMovieReviews(
            movieId = movieId,
            page = pageNumber
        )

    override suspend fun getTvShowReviews(
        tvShowId: Int,
        pageNumber: Int
    ): ApiResponse<ReviewResponse> =
        reviewsApiService.getTvShowReviews(
            tvShowId = tvShowId,
            page = pageNumber
        )
}
