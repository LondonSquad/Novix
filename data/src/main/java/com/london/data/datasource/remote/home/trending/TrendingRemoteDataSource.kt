package com.london.data.datasource.remote.home.trending

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.trending.model.TrendingMovieResponse

interface TrendingRemoteDataSource {
    suspend fun getTrendingMovies(page: Int): TrendingMovieResponse
}