package com.london.data.datasource.remote.home.trending

import com.london.data.datasource.remote.home.trending.model.TrendingResponse

interface TrendingRemoteDataSource {
    suspend fun getTrendingMovies(page: Int): TrendingResponse
    suspend fun getTrendingTvShows(page: Int): TrendingResponse
    suspend fun getTrendingActors(page: Int): TrendingResponse
}