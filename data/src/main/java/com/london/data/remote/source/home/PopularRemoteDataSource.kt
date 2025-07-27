package com.london.data.remote.source.home

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.model.PopularMovieResponse
import com.london.data.remote.model.home.model.PopularTvShowResponse


interface PopularRemoteDataSource {
    suspend fun getPopularMovies(): Result<ApiResponse<PopularMovieResponse>>
    suspend fun getPopularTvShows(): Result<ApiResponse<PopularTvShowResponse>>
}