package com.london.data.remote.source.discover

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.MovieRemote
import com.london.data.remote.model.search.SearchTvShowRemote
interface DiscoverRemoteDataSource {

    suspend fun getMoviesByCategory(
        categoryId: Int,
        pageNumber: Int,
        includeAdult: Boolean = false
    ): Result<ApiResponse<MovieRemote>>

    suspend fun getTvShowsByCategoryId(
        categoryId: Int,
        pageNumber: Int,
        includeAdult: Boolean = false
    ): Result<ApiResponse<SearchTvShowRemote>>
}
