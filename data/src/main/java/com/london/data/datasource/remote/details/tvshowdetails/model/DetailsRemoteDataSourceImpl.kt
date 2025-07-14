package com.london.data.datasource.remote.details.tvshowdetails.model

import com.london.data.BuildConfig
import com.london.data.datasource.remote.ApiConstants
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLProtocol
import io.ktor.http.path
import kotlinx.serialization.json.Json

class DetailsRemoteDataSourceImpl(private val ktorClient: HttpClient) : DetailsRemoteDataSource {
    override suspend fun getTvSeriesDetailsById(
        tvShowId: Int,
        language: String,
    ): TvShowDetailsRemoteResponse {
        val json = Json {
            ignoreUnknownKeys = true
        }
        val response = ktorClient.get {
            url {
                protocol = URLProtocol.Companion.HTTPS
                host = ApiConstants.HOST
                path(ApiConstants.getTvShowDetailsPath(tvShowId))
                parameters.append("language", language)
                parameters.append("api_key", BuildConfig.API_KEY)
            }
        }
        val responseBody = response.bodyAsText()
        return json.decodeFromString(responseBody)
    }
}