package com.london.data.remote.source.myrating

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.myrating.RatedMovieResponse
import com.london.data.remote.model.myrating.RatedTvShowResponse
import com.london.data.remote.service.myrating.MyRatingApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Inject

class MyRatingRemoteDataSourceImpl @Inject constructor(
    private val myRatingApiResponse: MyRatingApiService
) : MyRatingRemoteDataSource, BaseRemoteDatasource {

    override suspend fun getAllRatedMovies(
        accountId: Int,
        sessionId: String,
    ): Result<ApiResponse<RatedMovieResponse>> {
        return callApiWithRetry(
            apiCall = {
                myRatingApiResponse.getRatedMovies(
                    accountId = accountId,
                    sessionId = sessionId,
                )
            },
            mapper = { it }
        )
    }

    override suspend fun getAllRatedTvShows(
        accountId: Int,
        sessionId: String
    ): Result<ApiResponse<RatedTvShowResponse>> {
        return callApiWithRetry(
            apiCall = {
                myRatingApiResponse.getRatedTvShow(
                    accountId = accountId,
                    sessionId = sessionId,
                )
            },
            mapper = { it }
        )
    }
}