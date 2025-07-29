package com.london.data.remote.source.home.trending

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.model.trending.TrendingResponse

interface TrendingRemoteDataSource {
    suspend fun getTrendingMovies(page: Int): Result<ApiResponse<TrendingResponse>>
    suspend fun getTrendingTvShows(page: Int): Result<ApiResponse<TrendingResponse>>
    suspend fun getTrendingActors(page: Int): Result<ApiResponse<TrendingResponse>>
}
