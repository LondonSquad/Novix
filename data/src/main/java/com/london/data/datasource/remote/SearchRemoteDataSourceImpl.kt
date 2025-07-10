package com.london.data.datasource.remote

import android.util.Log
import com.london.data.dto.search.SearchActorsResponse
import com.london.data.dto.search.SearchMoviesResponse
import com.london.data.dto.search.SearchTvShowsResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.serialization.json.Json

class SearchRemoteDataSourceImpl(private val ktorClient: HttpClient) : RemoteDataSource {
    override suspend fun searchForMovies(): SearchMoviesResponse {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = ApiConstants.SEARCH_HOST
                path(ApiConstants.SEARCH_PATH_MOVIES)
                parameters.append("query", "Game")
                parameters.append("include_adult", "false")
                parameters.append("language", "en")
                parameters.append("page", "1")
                parameters.append("api_key", "YOUR_API_KEY")
            }
        }
        val responseBody = response.bodyAsText()
        Log.d("TAG", "search: $responseBody")
        return json.decodeFromString<SearchMoviesResponse>(responseBody)
    }

    override suspend fun searchForTvShows(): SearchTvShowsResponse {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = ApiConstants.SEARCH_HOST
                path(ApiConstants.SEARCH_PATH_TVS)
                parameters.append("query", "Game")
                parameters.append("include_adult", "false")
                parameters.append("language", "en")
                parameters.append("page", "1")
                parameters.append("api_key", "YOUR_API_KEY")
            }
        }
        val responseBody = response.bodyAsText()
        Log.d("TAG", "search: $responseBody")
        return json.decodeFromString<SearchTvShowsResponse>(responseBody)
    }

    override suspend fun searchForActors(): SearchActorsResponse {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = ApiConstants.SEARCH_HOST
                path(ApiConstants.SEARCH_PATH_ACTORS)
                parameters.append("query", "Jone")
                parameters.append("include_adult", "false")
                parameters.append("language", "en")
                parameters.append("page", "1")
                parameters.append("api_key", "YOUR_API_KEY")
            }
        }
        val responseBody = response.bodyAsText()
        Log.d("TAG", "search: $responseBody")
        return json.decodeFromString<SearchActorsResponse>(responseBody)
    }
}