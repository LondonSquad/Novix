package com.london.data.remote.source.myrating

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.myrating.RatedMovieResponse

interface MyRatingRemoteDataSource {
    suspend fun getAllRatedMovies(
        accountId: Int,
        sessionId: String,
    ): Result<ApiResponse<RatedMovieResponse>>
}