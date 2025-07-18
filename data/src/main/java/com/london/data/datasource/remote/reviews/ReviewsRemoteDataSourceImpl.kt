package com.london.data.datasource.remote.reviews

import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.reviews.model.ReviewResponse
import com.london.data.utils.get
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single
class ReviewsRemoteDataSourceImpl(
    private val ktorClient: HttpClient
) : ReviewsRemoteDataSource {
    override suspend fun getMovieReviews(
        movieId: Int,
        pageNumber: Int
    ): ApiResponse<ReviewResponse> =
        ktorClient.get(
            path = ApiConstants.getMovieReviewsPath(movieId),
            params = buildReviewParams(pageNumber = pageNumber)
        )

    override suspend fun getTvShowReviews(
        tvShowId: Int,
        pageNumber: Int
    ): ApiResponse<ReviewResponse> =
        ktorClient.get(
            path = ApiConstants.getTvShowReviewsPath(tvShowId),
            params = buildReviewParams(pageNumber = pageNumber)
        )
}

private fun buildReviewParams(
    language: String = "en-US",
    pageNumber: Int
): Map<String, String> = mapOf(
    "language" to language,
    "page" to pageNumber.toString()
)