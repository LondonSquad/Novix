package com.london.data.datasource.remote.search

import com.london.data.BuildConfig
import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsDto
import com.london.data.datasource.remote.details.actordetails.model.ActorMovieDetailsDto
import com.london.data.datasource.remote.details.actordetails.model.ActorTvShowDetailsDto
import com.london.data.datasource.remote.search.model.ApiSearch
import com.london.data.datasource.remote.search.model.SearchActorRemote
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.remote.search.model.SearchTvShowRemote
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.serialization.json.Json

class SearchRemoteDataSourceImpl(private val ktorClient: HttpClient) : RemoteDataSource {
    override suspend fun searchForMovies(
        query: String,
        includeAdult: Boolean,
        language: String,
        page: Int
    ): ApiSearch<SearchMovieRemote> {
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
                parameters.append("page", page.toString())
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
        page: Int
    ): ApiSearch<SearchTvShowRemote> {
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
                parameters.append("page", page.toString())
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
        page: Int
    ): ApiSearch<SearchActorRemote> {
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
                parameters.append("page", page.toString())
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        val responseBody = response.bodyAsText()
        return json.decodeFromString(responseBody)
    }

    override suspend fun getActorDetails(actorId: Int): ActorDetailsDto {
        TODO("Not yet implemented")
    }

    override suspend fun getActorMovies(actorId: Int): ActorMovieDetailsDto {
        TODO("Not yet implemented")
    }

    override suspend fun getActorTvShows(actorId: Int): ActorTvShowDetailsDto {
        TODO("Not yet implemented")
    }
}
