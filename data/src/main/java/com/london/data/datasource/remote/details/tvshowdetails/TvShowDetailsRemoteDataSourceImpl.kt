package com.london.data.datasource.remote.details.tvshowdetails

import android.util.Log
import com.london.data.BuildConfig
import com.london.data.datasource.device.DeviceConfigurationDataSource
import com.london.data.datasource.remote.ApiConstants
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowCastRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowDetailsRemoteResponse
import com.london.data.datasource.remote.details.tvshowdetails.model.TvShowImagesRemoteResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.serialization.json.Json

class TvShowDetailsRemoteDataSourceImpl(
    private val ktorClient: HttpClient,
    private val deviceConfigurationDataSource: DeviceConfigurationDataSource
) :
    TvShowDetailsRemoteDataSource {
    override suspend fun getTvShowDetailsById(
        tvShowId: Int,
    ): TvShowDetailsRemoteResponse {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.Companion.HTTPS
                host = ApiConstants.HOST
                path(ApiConstants.getTvShowDetailsPath(tvShowId))
                parameters.append("language", deviceConfigurationDataSource.getCurrentLanguage())
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        Log.d("TAG", "getTvShowDetailsById: ${deviceConfigurationDataSource.getCurrentLanguage()}")
        val responseBody = response.bodyAsText()
        return json.decodeFromString(responseBody)
    }

    override suspend fun getCastsByTvShowId(tvShowId: Int): TvShowCastRemoteResponse {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.Companion.HTTPS
                host = ApiConstants.HOST
                path(ApiConstants.getCastTvShowPath(tvShowId))
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        val responseBody = response.bodyAsText()
        return json.decodeFromString(responseBody)
    }

    override suspend fun getTvShowImagesById(tvShowId: Int): TvShowImagesRemoteResponse {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.Companion.HTTPS
                host = ApiConstants.HOST
                path(ApiConstants.getImagesTvShowPath(tvShowId))
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        val responseBody = response.bodyAsText()

        Log.d("TAG", "getTvShowImagesById: $responseBody")
        return json.decodeFromString(responseBody)
    }
}