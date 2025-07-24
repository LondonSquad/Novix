package com.london.data.datasource.remote.home.trending

import com.london.data.datasource.remote.home.trending.model.TrendingMovieResponse
import com.london.data.datasource.remote.home.trending.model.TrendingTvShowListResponse

interface TrendingRemoteDataSource {
    suspend fun getTrendingMovies(page: Int): TrendingMovieResponse
    suspend fun getTrendingTvShows(page: Int): TrendingTvShowListResponse
}