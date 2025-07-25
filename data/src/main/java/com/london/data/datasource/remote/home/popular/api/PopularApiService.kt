package com.london.data.datasource.remote.home.popular.api

import com.london.data.datasource.remote.ApiConstants.POPULAR_MOVIES_PATH
import com.london.data.datasource.remote.ApiConstants.POPULAR_TV_SHOWS_PATH
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.popular.model.PopularMovieResponse
import com.london.data.datasource.remote.home.popular.model.PopularTvShowResponse
import retrofit2.Response
import retrofit2.http.GET

interface PopularApiService {
    @GET(POPULAR_MOVIES_PATH)
    suspend fun getPopularMovies(): Response<ApiResponse<PopularMovieResponse>>

    @GET(POPULAR_TV_SHOWS_PATH)
    suspend fun getPopularTvShows(): Response<ApiResponse<PopularTvShowResponse>>
}