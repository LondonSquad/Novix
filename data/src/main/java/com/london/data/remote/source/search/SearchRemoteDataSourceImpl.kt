@file:KoverIgnore

package com.london.data.remote.source.search

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.MovieRemote
import com.london.data.remote.model.search.SearchTvShowRemote
import com.london.data.remote.model.search.searchactor.SearchActorRemote
import com.london.data.remote.service.search.SearchApiService
import com.london.data.remote.source.base.BaseRemoteDatasource
import com.london.domain.KoverIgnore
import javax.inject.Inject

@KoverIgnore
class SearchRemoteDataSourceImpl @Inject constructor(
    private val searchApiService: SearchApiService
) : SearchRemoteDataSource, BaseRemoteDatasource {

    override suspend fun searchForMovies(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): Result<ApiResponse<MovieRemote>> = callApiWithRetry(
        {
            searchApiService.searchMovies(
                query = query,
                includeAdult = includeAdult,
                page = pageNumber
            )
        },
        mapper = { it }
    )

    override suspend fun searchForTvShows(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): Result<ApiResponse<SearchTvShowRemote>> = callApiWithRetry(
        {
            searchApiService.searchTvShows(
                query = query,
                includeAdult = includeAdult,
                page = pageNumber
            )
        },
        mapper = { it }
    )

    override suspend fun searchForActors(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): Result<ApiResponse<SearchActorRemote>> = callApiWithRetry(
        {
            searchApiService.searchActors(
                query = query,
                includeAdult = includeAdult,
                page = pageNumber
            )
        },
        mapper = { it }
    )

    override suspend fun getMoviesByCategory(
        categoryId: Int,
        pageNumber: Int,
        includeAdult: Boolean
    ): Result<ApiResponse<MovieRemote>> = callApiWithRetry(
        {
            searchApiService.getMoviesByCategory(
                genreId = categoryId,
                page = pageNumber,
                includeAdult = includeAdult
            )
        },
        mapper = { it }
    )

    override suspend fun searchForTvShowsByCategoryId(
        categoryId: Int,
        pageNumber: Int,
        includeAdult: Boolean
    ): Result<ApiResponse<SearchTvShowRemote>> =callApiWithRetry(
        {
            searchApiService.searchForTvShowsByCategory(
                genreId = categoryId,
                page = pageNumber,
                includeAdult = includeAdult
            )
        },
        mapper = { it }
    )
}
