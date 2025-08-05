package com.london.data.remote.service.details.tvshow

import com.london.data.remote.model.details.rating.RatingRemoteBody
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AddTvShowRatingApiService {
    @POST("3/tv/{tv_id}/rating")
    fun addTvShowRating(
        @Path("series_id") tvSeriesId: Int,
        @Query("guest_session_id") guestSessionId: String?,
        @Query("session_id") userSessionId: String?,
        @Body ratingRequest: RatingRemoteBody
    ): Response<RatingRemoteResponse>
}