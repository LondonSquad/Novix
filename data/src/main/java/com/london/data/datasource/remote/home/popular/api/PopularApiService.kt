package com.london.data.datasource.remote.home.popular.api

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.popular.model.PopularMovieResponse
import retrofit2.http.GET

interface PopularApiService {
    @GET("3/movie/popular")
    suspend fun getPopularMovies(): ApiResponse<PopularMovieResponse>
}