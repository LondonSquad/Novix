@file:KoverIgnore
package com.london.data.datasource.remote.search.api

import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
import com.london.domain.KoverIgnore
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {

    @GET(ApiConstants.SEARCH_PATH_MOVIES)
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("page") page: Int
    ): ApiResponse<SearchMovieRemote>

    @GET(ApiConstants.SEARCH_PATH_TVS)
    suspend fun searchTvShows(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("page") page: Int
    ): ApiResponse<SearchTvShowRemote>

    @GET(ApiConstants.SEARCH_PATH_ACTORS)
    suspend fun searchActors(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("page") page: Int
    ): ApiResponse<SearchActorRemote>

    @GET(ApiConstants.MOVIE_DISCOVER_PATH)
    suspend fun getMoviesByCategory(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean
    ): ApiResponse<SearchMovieRemote>

    @GET(ApiConstants.MOVIE_DISCOVER_PATH)
    suspend fun getUpComingMoviesByCategory(
        @Query("with_genres") genreId: Int?=null,
        @Query("primary_release_date.gte") releaseDate: String,
        @Query("sort_by") sortBy: String = "primary_release_date.asc",
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean
    ): ApiResponse<SearchMovieRemote>
}
