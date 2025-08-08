package com.london.data.remote.service.myrating

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.myrating.RatedMovieResponse
import com.london.data.remote.model.myrating.RatedTvShowResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyRatingApiService {
    @GET("3/account/{account_id}/rated/movies")
    suspend fun getRatedMovies(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
    ): Response<ApiResponse<RatedMovieResponse>>

    @GET("3/account/{account_id}/rated/tv")
    suspend fun getRatedTvShow(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
    ): Response<ApiResponse<RatedTvShowResponse>>
}