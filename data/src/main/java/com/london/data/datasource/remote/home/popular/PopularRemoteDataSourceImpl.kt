package com.london.data.datasource.remote.home.popular

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.popular.api.PopularApiService
import com.london.data.datasource.remote.home.popular.model.PopularMovieResponse

class PopularRemoteDataSourceImpl(
    private val popularApiService: PopularApiService
) : PopularRemoteDataSource {
    override suspend fun getPopularMovies(): ApiResponse<PopularMovieResponse> {
        return popularApiService.getPopularMovies()
    }
}