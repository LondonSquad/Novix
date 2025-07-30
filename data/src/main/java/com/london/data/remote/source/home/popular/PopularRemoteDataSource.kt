package com.london.data.remote.source.home.popular

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.model.popular.PopularMovieResponse
import com.london.data.remote.model.home.model.popular.PopularTvShowResponse


interface PopularRemoteDataSource {
    suspend fun getPopularMovies(): Result<ApiResponse<PopularMovieResponse>>
    suspend fun getPopularTvShows(): Result<ApiResponse<PopularTvShowResponse>>
}