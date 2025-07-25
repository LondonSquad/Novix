@file:KoverIgnore

package com.london.data.datasource.remote.toprated.movie.api

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.toprated.movie.model.TopRatedMovieRemote
import com.london.domain.KoverIgnore
import retrofit2.http.GET
import retrofit2.http.Query

interface TopRatedMovieApiService {
    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") pageNumber: Int,
    ): ApiResponse<TopRatedMovieRemote>
}
