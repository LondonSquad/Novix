package com.london.data.remote.service.home

import com.london.data.remote.model.ApiConstants
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.MovieRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UpComingApiService {
    @GET(ApiConstants.MOVIE_DISCOVER_PATH)
    suspend fun getUpComingMoviesByCategory(
        @Query("with_genres") genreId: Int?=null,
        @Query("primary_release_date.gte") releaseDate: String,
        @Query("sort_by") sortBy: String = "primary_release_date.asc",
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean
    ): Response<ApiResponse<MovieRemote>>
}