package com.london.data.remote.source.myrating

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.myrating.RatedMediaResponse

interface MyRatingRemoteDataSource {
    suspend fun getAllRatedMovies(
        accountId: Int,
        sessionId: String,
    ): Result<ApiResponse<RatedMediaResponse>>

    suspend fun getAllRatedTvShows(
        accountId: Int,
        sessionId: String,
    ): Result<ApiResponse<RatedMediaResponse>>
}