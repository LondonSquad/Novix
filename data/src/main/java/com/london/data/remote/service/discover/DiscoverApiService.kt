@file:KoverIgnore
package com.london.data.remote.service.discover

import com.london.data.remote.model.ApiConstants
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.model.MovieRemote
import com.london.data.remote.model.search.model.SearchTvShowRemote
import com.london.domain.KoverIgnore
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverApiService {
    @GET(ApiConstants.MOVIE_DISCOVER_PATH)
    suspend fun getMoviesByCategory(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean
    ): Response<ApiResponse<MovieRemote>>


    @GET(ApiConstants.TV_SHOW_DISCOVER_PATH)
    suspend fun searchForTvShowsByCategory(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean
    ): Response<ApiResponse<SearchTvShowRemote>>
}