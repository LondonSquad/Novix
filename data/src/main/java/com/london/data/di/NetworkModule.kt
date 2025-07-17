package com.london.data.di

import android.content.Context
import android.util.Log
import com.london.data.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class NetworkModule {
    @Single
    fun provideKtorClient(
        context: Context
    ): HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
        defaultRequest {
            url(urlString = BuildConfig.BASE_URL)
            url.parameters.append("api_key", BuildConfig.API_KEY)

            header(
                key = "language",
                value = context.resources.configuration.locales[0].language
            )
            header(
                key = "Authorization",
                value = "Bearer ${BuildConfig.AUTHORIZATION_KEY}"
            )
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.i("DEBUGGING", message)
                }
            }
            level = LogLevel.ALL
        }
    }
}
