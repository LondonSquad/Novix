@file:KoverIgnore

package com.london.data.remote.source.discover

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.MovieRemote
import com.london.data.remote.model.search.SearchTvShowRemote
import com.london.data.remote.service.discover.DiscoverApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import com.london.domain.KoverIgnore
import javax.inject.Inject

class DiscoverRemoteDataSourceImpl @Inject constructor(
    private val discoverApiService: DiscoverApiService
) : DiscoverRemoteDataSource, BaseRemoteDatasource {
    override suspend fun getMoviesByCategory(
        categoryId: Int,
        pageNumber: Int,
        includeAdult: Boolean
    ): Result<ApiResponse<MovieRemote>> = callApiWithRetry(
        apiCall = {
            discoverApiService.getMoviesByCategory(
                genreId = categoryId,
                page = pageNumber,
                includeAdult = includeAdult
            )
        },
        mapper = { it }
    )

    override suspend fun getTvShowsByCategoryId(
        categoryId: Int,
        pageNumber: Int,
        includeAdult: Boolean
    ): Result<ApiResponse<SearchTvShowRemote>> = callApiWithRetry(
        apiCall = {
            discoverApiService.searchForTvShowsByCategory(
                genreId = categoryId,
                page = pageNumber,
                includeAdult = includeAdult
            )
        },
        mapper = { it }
    )
}