package com.london.data.datasource.remote.search

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote

interface SearchRemoteDataSource {
    suspend fun searchForMovies(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): ApiResponse<SearchMovieRemote>

    suspend fun searchForTvShows(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): ApiResponse<SearchTvShowRemote>

    suspend fun searchForActors(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): ApiResponse<SearchActorRemote>

    suspend fun getMoviesByCategory(
        categoryId: Int,
        pageNumber: Int,
        includeAdult: Boolean = false
    ): ApiResponse<SearchMovieRemote>

    suspend fun getUpComingMoviesByCategory(
        categoryId: Int?=null,
        pageNumber: Int,
        includeAdult: Boolean = false
    ): ApiResponse<SearchMovieRemote>
}