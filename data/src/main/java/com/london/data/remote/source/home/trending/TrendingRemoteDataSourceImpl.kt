package com.london.data.remote.source.home.trending

import com.london.data.remote.model.trending.TrendingRemote
import com.london.data.remote.service.home.trending.TrendingApiService
import org.koin.core.annotation.Single

@Single
class TrendingRemoteDataSourceImpl(
    private val trendingApiService: TrendingApiService
) : TrendingRemoteDataSource {
    override suspend fun getTrendingMovies(page: Int): TrendingRemote =
        trendingApiService.getTrendingMovies(page)

    override suspend fun getTrendingTvShows(page: Int): TrendingRemote =
        trendingApiService.getTrendingTvShows(page)

    override suspend fun getTrendingActors(page: Int) = trendingApiService.getTrendingActors(page)
} 