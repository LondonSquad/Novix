package com.london.data.datasource.remote.home.trending.api

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.trending.model.TrendingMovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendingApiService {
    @GET("3/trending/movie/day")
    suspend fun getTrendingMovies(@Query("page") page: Int): TrendingMovieResponse
} 