@file:KoverIgnore

package com.london.data.remote.source.search

import com.london.data.remote.model.ApiResponse
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
import com.london.domain.KoverIgnore

interface SearchRemoteDataSource {
    suspend fun searchForMovies(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): Result<ApiResponse<SearchMovieRemote>>

    suspend fun searchForTvShows(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): Result<ApiResponse<SearchTvShowRemote>>

    suspend fun searchForActors(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): Result<ApiResponse<SearchActorRemote>>

    suspend fun getMoviesByCategory(
        categoryId: Int,
        pageNumber: Int,
        includeAdult: Boolean = false
    ): Result<ApiResponse<SearchMovieRemote>>

    suspend fun getUpComingMoviesByCategory(
        categoryId: Int?=null,
        pageNumber: Int,
        includeAdult: Boolean = false
    ): Result<ApiResponse<SearchMovieRemote>>
}