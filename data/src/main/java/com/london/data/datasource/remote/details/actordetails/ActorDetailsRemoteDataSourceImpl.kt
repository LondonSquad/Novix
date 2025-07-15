package com.london.data.datasource.remote.details.actordetails

import android.util.Log
import com.london.data.BuildConfig
import com.london.data.datasource.device.DeviceConfigurationDataSource
import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsRemoteResponse
import com.london.data.datasource.remote.details.actordetails.model.ActorImageResponse
import com.london.data.datasource.remote.details.actordetails.model.ActorMovieDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.ActorTvShowDetailsResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.serialization.json.Json

class ActorDetailsRemoteDataSourceImpl(
    private val ktorClient: HttpClient,
    private val deviceConfigurationDataSource: DeviceConfigurationDataSource
) :
    ActorDetailsRemoteDataSource {
    override suspend fun getActorDetailsById(
        actorId: Int
    ): ActorDetailsRemoteResponse {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.Companion.HTTPS
                host = ApiConstants.HOST
                path(ApiConstants.getActorDetailsPath(actorId))
                parameters.append("language", deviceConfigurationDataSource.getCurrentLanguage())
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }

        val responseBody = response.bodyAsText()
        Log.d("ActorDetails", "Response body: $responseBody")

        return json.decodeFromString(responseBody)
    }

    override suspend fun getActorMovieById(actorId: Int): ActorMovieDetailsResponse {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.Companion.HTTPS
                host = ApiConstants.HOST
                path(ApiConstants.getActorMoviesPath(actorId))
                parameters.append("language", deviceConfigurationDataSource.getCurrentLanguage())
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }

        val responseBody = response.bodyAsText()
        Log.d("ActorDetails", "Response body: $responseBody")

        return json.decodeFromString(responseBody)
    }

    override suspend fun getActorTvShowById(actorId: Int): ActorTvShowDetailsResponse {

        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.Companion.HTTPS
                host = ApiConstants.HOST
                path(ApiConstants.getActorTvShowsPath(actorId))
                parameters.append("language", deviceConfigurationDataSource.getCurrentLanguage())
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }

        val responseBody = response.bodyAsText()
        Log.d("ActorDetails", "Response body: $responseBody")

        return json.decodeFromString(responseBody)
    }

    override suspend fun getActorImagePath(actorId: Int): ActorImageResponse {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.Companion.HTTPS
                host = ApiConstants.HOST
                path(ApiConstants.getActorImagePath(actorId))
                parameters.append("language", deviceConfigurationDataSource.getCurrentLanguage())
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }

        val responseBody = response.bodyAsText()
        Log.d("ActorDetails", "Response body: $responseBody")

        return json.decodeFromString(responseBody)
    }
}