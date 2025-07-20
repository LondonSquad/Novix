package com.london.data.datasource.remote.search.api

import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {

    @GET(ApiConstants.SEARCH_PATH_MOVIES)
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("language") language: String,
        @Query("page") page: Int
    ): ApiResponse<SearchMovieRemote>

    @GET(ApiConstants.SEARCH_PATH_TVS)
    suspend fun searchTvShows(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("language") language: String,
        @Query("page") page: Int
    ): ApiResponse<SearchTvShowRemote>

    @GET(ApiConstants.SEARCH_PATH_ACTORS)
    suspend fun searchActors(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("language") language: String,
        @Query("page") page: Int
    ): ApiResponse<SearchActorRemote>

    @GET(ApiConstants.SEARCH_BY_CATEGORY_PATH)
    suspend fun getMoviesByCategory(
        @Query("with_genres") genreId: Int,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean
    ): ApiResponse<SearchMovieRemote>
}
