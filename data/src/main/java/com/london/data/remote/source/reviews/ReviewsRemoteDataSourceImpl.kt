package com.london.data.remote.source.reviews

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.BaseRemoteDatasource
import com.london.data.datasource.remote.reviews.api.ReviewsApiService
import com.london.data.remote.model.reviews.model.ReviewResponse
import org.koin.core.annotation.Single

@Single
class ReviewsRemoteDataSourceImpl(
    private val reviewsApiService: ReviewsApiService
) : ReviewsRemoteDataSource, BaseRemoteDatasource {

    override suspend fun getMovieReviews(
        movieId: Int, pageNumber: Int
    ): Result<ApiResponse<ReviewResponse>> = callApiWithRetry(
        apiCall = { reviewsApiService.getMovieReviews(movieId = movieId, page = pageNumber) },
        mapper = { it }
    )

    override suspend fun getTvShowReviews(
        tvShowId: Int, pageNumber: Int
    ): Result<ApiResponse<ReviewResponse>> = callApiWithRetry(
        apiCall = { reviewsApiService.getTvShowReviews(tvShowId = tvShowId, page = pageNumber) },
        mapper = { it }
    )
}
