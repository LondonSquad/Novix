@file:KoverIgnore
package com.london.data.remote.service.search

import com.london.data.remote.model.ApiConstants
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.MovieRemote
import com.london.data.remote.model.search.SearchTvShowRemote
import com.london.data.remote.model.search.searchactor.SearchActorRemote
import com.london.domain.KoverIgnore
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {

    @GET(ApiConstants.SEARCH_PATH_MOVIES)
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("page") page: Int
    ): Response<ApiResponse<MovieRemote>>

    @GET(ApiConstants.SEARCH_PATH_TVS)
    suspend fun searchTvShows(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("page") page: Int
    ): Response<ApiResponse<SearchTvShowRemote>>

    @GET(ApiConstants.SEARCH_PATH_ACTORS)
    suspend fun searchActors(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("page") page: Int
    ): Response<ApiResponse<SearchActorRemote>>

}
