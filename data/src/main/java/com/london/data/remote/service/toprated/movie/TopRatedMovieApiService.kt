@file:KoverIgnore

package com.london.data.remote.service.toprated.movie

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.toprated.movie.model.TopRatedMovieRemote
import com.london.domain.KoverIgnore
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TopRatedMovieApiService {
    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") pageNumber: Int,
    ): Response<ApiResponse<TopRatedMovieRemote>>
}
