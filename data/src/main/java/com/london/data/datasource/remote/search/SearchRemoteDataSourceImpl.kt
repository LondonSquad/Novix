@file:KoverIgnore

package com.london.data.datasource.remote.search

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.BaseRemoteDatasource
import com.london.data.datasource.remote.search.api.SearchApiService
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
import com.london.data.utils.getCurrentDate
import com.london.domain.KoverIgnore
import org.koin.core.annotation.Single

@Single
@KoverIgnore
class SearchRemoteDataSourceImpl(
    private val searchApiService: SearchApiService
) : SearchRemoteDataSource, BaseRemoteDatasource {

    override suspend fun searchForMovies(
        query: String, includeAdult: Boolean, pageNumber: Int
    ): Result<ApiResponse<SearchMovieRemote>> = callApiWithRetry(
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
        query: String, includeAdult: Boolean, pageNumber: Int
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
        query: String, includeAdult: Boolean, pageNumber: Int
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
        categoryId: Int, pageNumber: Int, includeAdult: Boolean
    ): Result<ApiResponse<SearchMovieRemote>> = callApiWithRetry(
        {
            searchApiService.getMoviesByCategory(
                genreId = categoryId,
                page = pageNumber,
                includeAdult = includeAdult
            )
        },
        mapper = { it }
    )

    override suspend fun getUpComingMoviesByCategory(
        categoryId: Int?, pageNumber: Int, includeAdult: Boolean
    ): Result<ApiResponse<SearchMovieRemote>> =
        callApiWithRetry(
            {
                searchApiService.getUpComingMoviesByCategory(
                    genreId = categoryId,
                    releaseDate = getCurrentDate(),
                    page = pageNumber,
                    includeAdult = includeAdult
                )
            },
            mapper = { it }
        )
}
