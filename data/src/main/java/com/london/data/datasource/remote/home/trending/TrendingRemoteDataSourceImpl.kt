package com.london.data.datasource.remote.home.trending

import com.london.data.datasource.remote.home.trending.api.TrendingApiService
import com.london.data.datasource.remote.home.trending.model.TrendingMovieResponse
import org.koin.core.annotation.Single

@Single
class TrendingRemoteDataSourceImpl(
    private val trendingApiService: TrendingApiService
) : TrendingRemoteDataSource {
    override suspend fun getTrendingMovies(page: Int): TrendingMovieResponse {
        return trendingApiService.getTrendingMovies(page)
    }
} 