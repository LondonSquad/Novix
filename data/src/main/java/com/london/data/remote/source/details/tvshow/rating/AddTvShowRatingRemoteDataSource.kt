package com.london.data.remote.source.details.tvshow.rating

import com.london.data.remote.model.details.rating.RatingRemoteResponse

interface AddTvShowRatingRemoteDataSource {
    suspend fun addMTvShowRating(
        tvSeriesId: Int,
        rating: Double,
        guestSessionId: String?,
        userSessionId: String?
    ): Result<RatingRemoteResponse>
}