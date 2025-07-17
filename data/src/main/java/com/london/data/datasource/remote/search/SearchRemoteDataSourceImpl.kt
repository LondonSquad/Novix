package com.london.data.datasource.remote.search

import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
import com.london.data.utils.get
import io.ktor.client.HttpClient

class SearchRemoteDataSourceImpl(
    private val ktorClient: HttpClient
) : SearchRemoteDataSource {

    override suspend fun searchForMovies(
        query: String,
        includeAdult: Boolean,
        language: String,
        pageNumber: Int
    ): ApiResponse<SearchMovieRemote> =
        ktorClient.get(
            path = ApiConstants.SEARCH_PATH_MOVIES,
            params = buildSearchParams(query, includeAdult, language, pageNumber)
        )

    override suspend fun searchForTvShows(
        query: String,
        includeAdult: Boolean,
        language: String,
        pageNumber: Int
    ): ApiResponse<SearchTvShowRemote> =
        ktorClient.get(
            path = ApiConstants.SEARCH_PATH_TVS,
            params = buildSearchParams(query, includeAdult, language, pageNumber)
        )

    override suspend fun searchForActors(
        query: String,
        includeAdult: Boolean,
        language: String,
        pageNumber: Int
    ): ApiResponse<SearchActorRemote> =
        ktorClient.get(
            path = ApiConstants.SEARCH_PATH_ACTORS,
            params = buildSearchParams(query, includeAdult, language, pageNumber)
        )
}

private fun buildSearchParams(
    query: String,
    includeAdult: Boolean,
    language: String,
    pageNumber: Int
): Map<String, String> = mapOf(
    "query" to query,
    "include_adult" to includeAdult.toString(),
    "language" to language,
    "page" to pageNumber.toString()
)
