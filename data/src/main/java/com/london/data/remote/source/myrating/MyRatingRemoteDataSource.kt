package com.london.data.remote.source.myrating

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.myrating.RatedMovieResponse
import com.london.data.remote.model.myrating.RatedTvShowResponse

interface MyRatingRemoteDataSource {
    suspend fun getAllRatedMovies(
        accountId: Int,
        sessionId: String,
    ): Result<ApiResponse<RatedMovieResponse>>

    suspend fun getAllRatedTvShow(
        accountId: Int,
        sessionId: String,
    ): Result<ApiResponse<RatedTvShowResponse>>
}