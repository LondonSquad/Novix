package com.london.data.remote.source.home.trending

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.trending.TrendingResponse
import retrofit2.Response

interface TrendingRemoteDataSource {
    suspend fun getTrendingMovies(page: Int): Response<ApiResponse<TrendingResponse>>
    suspend fun getTrendingTvShows(page: Int): Response<ApiResponse<TrendingResponse>>
    suspend fun getTrendingActors(page: Int): Response<ApiResponse<TrendingResponse>>
}