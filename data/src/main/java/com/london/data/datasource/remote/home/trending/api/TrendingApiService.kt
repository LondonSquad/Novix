package com.london.data.datasource.remote.home.trending.api

import com.london.data.datasource.remote.home.trending.model.TrendingMovieResponse
import com.london.data.datasource.remote.home.trending.model.TrendingTvShowListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendingApiService {
    @GET("3/trending/movie/day")
    suspend fun getTrendingMovies(@Query("page") page: Int): TrendingMovieResponse

    @GET("3/trending/tv/day")
    suspend fun getTrendingTvShows(@Query("page") page: Int): TrendingTvShowListResponse
} 