package com.london.data.datasource.remote.search

import com.london.data.BuildConfig
import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class SearchRemoteDataSourceImpl(private val ktorClient: HttpClient) : SearchRemoteDataSource {
    override suspend fun searchForMovies(
        query: String,
        includeAdult: Boolean,
        language: String,
        pageNumber: Int
    ): ApiResponse<SearchMovieRemote> {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.Companion.HTTPS
                host = ApiConstants.HOST
                path(ApiConstants.SEARCH_PATH_MOVIES)
                parameters.append("query", query)
                parameters.append("include_adult", includeAdult.toString())
                parameters.append("language", language)
                parameters.append("page", pageNumber.toString())
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        val responseBody = response.bodyAsText()
        return json.decodeFromString(responseBody)
    }

    override suspend fun searchForTvShows(
        query: String,
        includeAdult: Boolean,
        language: String,
        pageNumber: Int
    ): ApiResponse<SearchTvShowRemote> {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.Companion.HTTPS
                host = ApiConstants.HOST
                path(ApiConstants.SEARCH_PATH_TVS)
                parameters.append("query", query)
                parameters.append("include_adult", includeAdult.toString())
                parameters.append("language", language)
                parameters.append("page", pageNumber.toString())
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        val responseBody = response.bodyAsText()
        return json.decodeFromString(responseBody)
    }

    override suspend fun searchForActors(
        query: String,
        includeAdult: Boolean,
        language: String,
        pageNumber: Int
    ): ApiResponse<SearchActorRemote> {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.Companion.HTTPS
                host = ApiConstants.HOST
                path(ApiConstants.SEARCH_PATH_ACTORS)
                parameters.append("query", query)
                parameters.append("include_adult", includeAdult.toString())
                parameters.append("language", language)
                parameters.append("page", pageNumber.toString())
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        val responseBody = response.bodyAsText()
        return json.decodeFromString(responseBody)
    }
}