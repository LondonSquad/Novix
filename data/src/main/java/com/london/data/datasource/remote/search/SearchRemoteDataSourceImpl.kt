@file:KoverIgnore
package com.london.data.datasource.remote.search

import com.london.data.datasource.remote.ApiResponse
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
) : SearchRemoteDataSource {

    override suspend fun searchForMovies(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): ApiResponse<SearchMovieRemote> =
        searchApiService.searchMovies(
            query = query,
            includeAdult = includeAdult,
            page = pageNumber
        )

    override suspend fun searchForTvShows(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): ApiResponse<SearchTvShowRemote> =
        searchApiService.searchTvShows(
            query = query,
            includeAdult = includeAdult,
            page = pageNumber
        )

    override suspend fun searchForActors(
        query: String,
        includeAdult: Boolean,
        pageNumber: Int
    ): ApiResponse<SearchActorRemote> =
        searchApiService.searchActors(
            query = query,
            includeAdult = includeAdult,
            page = pageNumber
        )

    override suspend fun getMoviesByCategory(
        categoryId: Int,
        pageNumber: Int,
        includeAdult: Boolean
    ): ApiResponse<SearchMovieRemote> = searchApiService.getMoviesByCategory(
        genreId = categoryId,
        page = pageNumber,
        includeAdult = includeAdult
    )

    override suspend fun getUpComingMoviesByCategory(
        categoryId: Int?,
        pageNumber: Int,
        includeAdult: Boolean
    ): ApiResponse<SearchMovieRemote> =
        searchApiService.getUpComingMoviesByCategory(
            genreId = categoryId,
            releaseDate = getCurrentDate(),
            page = pageNumber,
            includeAdult = includeAdult
        )
}
