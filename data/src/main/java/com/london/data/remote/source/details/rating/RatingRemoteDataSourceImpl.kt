package com.london.data.remote.source.details.rating

import com.london.data.remote.model.details.rating.RatingRemoteBody
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.service.details.rating.RatingApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class RatingRemoteDataSourceImpl @Inject constructor(
    private val ratingApiService: RatingApiService
) : RatingRemoteDataSource, BaseRemoteDatasource {
    override suspend fun addMovieRating(
        movieId: Int,
        rating: Double,
        guestSessionId: String?,
        userSessionId: String?
    ): Result<RatingRemoteResponse> = callApiWithRetry(apiCall = {
        ratingApiService.addMovieRating(
            movieId = movieId,
            ratingRequest = RatingRemoteBody(rating),
            guestSessionId = guestSessionId,
            userSessionId = userSessionId
        )
    }, mapper = { it })
}