package com.london.data.remote.source.home.trending

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.trending.TrendingResponse
import com.london.data.remote.service.home.trending.TrendingApiService
import org.koin.core.annotation.Single
import retrofit2.Response

@Single
class TrendingRemoteDataSourceImpl(
    private val trendingApiService: TrendingApiService
) : TrendingRemoteDataSource {
    override suspend fun getTrendingMovies(page: Int): Response<ApiResponse<TrendingResponse>> =
        trendingApiService.getTrendingMovies(page)

    override suspend fun getTrendingTvShows(page: Int): Response<ApiResponse<TrendingResponse>> =
        trendingApiService.getTrendingTvShows(page)

    override suspend fun getTrendingActors(page: Int): Response<ApiResponse<TrendingResponse>> =
        trendingApiService.getTrendingActors(page)
} 