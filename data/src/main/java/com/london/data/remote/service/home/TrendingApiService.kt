package com.london.data.remote.service.home

import com.london.data.remote.model.ApiConstants
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.trending.TrendingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendingApiService {
    @GET(ApiConstants.TRENDING_MOVIES_PATH)
    suspend fun getTrendingMovies(@Query("page") page: Int): Response<ApiResponse<TrendingResponse>>

    @GET(ApiConstants.TRENDING_TV_SHOWS_PATH)
    suspend fun getTrendingTvShows(@Query("page") page: Int): Response<ApiResponse<TrendingResponse>>

    @GET(ApiConstants.TRENDING_ACTORS_PATH)
    suspend fun getTrendingActors(@Query("page") page: Int): Response<ApiResponse<TrendingResponse>>
}
