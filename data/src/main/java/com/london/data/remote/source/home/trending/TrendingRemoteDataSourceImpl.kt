package com.london.data.remote.source.home.trending

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.model.trending.TrendingResponse
import com.london.data.remote.service.home.TrendingApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import javax.inject.Singleton

@Singleton
class TrendingRemoteDataSourceImpl(
    private val trendingApiService: TrendingApiService
) : TrendingRemoteDataSource, BaseRemoteDatasource {
    override suspend fun getTrendingMovies(page: Int): Result<ApiResponse<TrendingResponse>> =
        callApiWithRetry(
            apiCall = { trendingApiService.getTrendingMovies(page =  page) },
            mapper = { it }
        )

    override suspend fun getTrendingTvShows(page: Int): Result<ApiResponse<TrendingResponse>> =
        callApiWithRetry(
            apiCall = { trendingApiService.getTrendingTvShows(page = page) },
            mapper = { it }
        )

    override suspend fun getTrendingActors(page: Int): Result<ApiResponse<TrendingResponse>> =
        callApiWithRetry(
            apiCall = { trendingApiService.getTrendingActors(page = page) },
            mapper = { it }
        )
}