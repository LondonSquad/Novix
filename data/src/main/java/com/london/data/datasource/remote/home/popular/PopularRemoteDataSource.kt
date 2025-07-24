package com.london.data.datasource.remote.home.popular

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.popular.model.PopularMovieResponse
import com.london.data.datasource.remote.home.popular.model.PopularTvShowResponse

interface PopularRemoteDataSource {
    suspend fun getPopularMovies(): ApiResponse<PopularMovieResponse>
    suspend fun getPopularTvShows(): ApiResponse<PopularTvShowResponse>
}