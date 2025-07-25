package com.london.data.datasource.remote.home.trending

import com.london.data.datasource.remote.home.trending.api.TrendingApiService
import com.london.data.datasource.remote.home.trending.model.TrendingResponse
import org.koin.core.annotation.Single

@Single
class TrendingRemoteDataSourceImpl(
    private val trendingApiService: TrendingApiService
) : TrendingRemoteDataSource {
    override suspend fun getTrendingMovies(page: Int): TrendingResponse =
        trendingApiService.getTrendingMovies(page)

    override suspend fun getTrendingTvShows(page: Int): TrendingResponse =
        trendingApiService.getTrendingTvShows(page)

    override suspend fun getTrendingActors(page: Int) = trendingApiService.getTrendingActors(page)
} 