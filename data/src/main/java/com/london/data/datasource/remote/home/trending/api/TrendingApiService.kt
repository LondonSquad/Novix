package com.london.data.datasource.remote.home.trending.api

import com.london.data.datasource.remote.home.trending.model.TrendingResponse
import retrofit2.http.GET
import retrofit2.http.Query

import com.london.data.datasource.remote.ApiConstants

interface TrendingApiService {
    @GET(ApiConstants.TRENDING_MOVIES_PATH)
    suspend fun getTrendingMovies(@Query("page") page: Int): TrendingResponse

    @GET(ApiConstants.TRENDING_TV_SHOWS_PATH)
    suspend fun getTrendingTvShows(@Query("page") page: Int): TrendingResponse

    @GET(ApiConstants.TRENDING_ACTORS_PATH)
    suspend fun getTrendingActors(@Query("page") page: Int): TrendingResponse
}