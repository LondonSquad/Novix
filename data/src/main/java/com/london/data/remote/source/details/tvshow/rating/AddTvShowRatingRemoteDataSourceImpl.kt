package com.london.data.remote.source.details.tvshow.rating

import com.london.data.remote.model.details.rating.RatingRemoteBody
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.service.details.tvshow.AddTvShowRatingApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class AddTvShowRatingRemoteDataSourceImpl @Inject constructor(
    private val addTvShowRatingApiService: AddTvShowRatingApiService
) : AddTvShowRatingRemoteDataSource, BaseRemoteDatasource {
    override suspend fun addMTvShowRating(
        tvSeriesId: Int,
        rating: Double,
        guestSessionId: String?,
        userSessionId: String?
    ): Result<RatingRemoteResponse> = callApiWithRetry(
        apiCall = {
            addTvShowRatingApiService.addTvShowRating(
                tvSeriesId = tvSeriesId,
                guestSessionId = guestSessionId,
                userSessionId = userSessionId,
                ratingRequest = RatingRemoteBody(rating)
            )
        }, mapper = { it })
}