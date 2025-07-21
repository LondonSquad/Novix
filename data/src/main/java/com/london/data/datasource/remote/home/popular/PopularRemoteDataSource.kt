package com.london.data.datasource.remote.home.popular

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.popular.model.PopularMovieResponse

interface PopularRemoteDataSource {
    suspend fun getPopularMovies(): ApiResponse<PopularMovieResponse>
}