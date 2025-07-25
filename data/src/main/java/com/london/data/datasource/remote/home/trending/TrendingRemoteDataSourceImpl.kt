package com.london.data.datasource.remote.home.trending

import com.london.data.datasource.remote.home.trending.api.TrendingApiService
import com.london.data.datasource.remote.home.trending.model.TrendingMovieResponse
import com.london.data.datasource.remote.home.trending.model.TrendingTvShowListResponse
import org.koin.core.annotation.Single

@Single
class TrendingRemoteDataSourceImpl(
    private val trendingApiService: TrendingApiService
) : TrendingRemoteDataSource {
    override suspend fun getTrendingMovies(page: Int): TrendingMovieResponse {
        return trendingApiService.getTrendingMovies(page)
    }

    override suspend fun getTrendingTvShows(page: Int): TrendingTvShowListResponse {
        return trendingApiService.getTrendingTvShows(page)
    }

    override suspend fun getTrendingActors(page: Int) = trendingApiService.getTrendingActors(page)
} 