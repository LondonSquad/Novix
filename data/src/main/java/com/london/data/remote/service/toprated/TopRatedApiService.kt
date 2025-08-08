@file:KoverIgnore

package com.london.data.remote.service.toprated

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.toprated.TopRatedMovieRemote
import com.london.data.remote.model.home.toprated.TopRatedTvSeriesRemote
import com.london.domain.KoverIgnore
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TopRatedApiService {

    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") pageNumber: Int,
    ): Response<ApiResponse<TopRatedMovieRemote>>

    @GET("3/tv/top_rated")
    suspend fun getTopRatedTvSeries(
        @Query("page") pageNumber: Int,
    ): Response<ApiResponse<TopRatedTvSeriesRemote>>
}