package com.london.data.remote.service.home

import com.london.data.remote.model.ApiConstants.POPULAR_MOVIES_PATH
import com.london.data.remote.model.ApiConstants.POPULAR_TV_SHOWS_PATH
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.model.PopularMovieResponse
import com.london.data.remote.model.home.model.PopularTvShowResponse
import retrofit2.Response
import retrofit2.http.GET

interface PopularApiService {
    @GET(POPULAR_MOVIES_PATH)
    suspend fun getPopularMovies(): Response<ApiResponse<PopularMovieResponse>>

    @GET(POPULAR_TV_SHOWS_PATH)
    suspend fun getPopularTvShows(): Response<ApiResponse<PopularTvShowResponse>>
}