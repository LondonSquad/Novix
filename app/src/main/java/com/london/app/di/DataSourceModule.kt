package com.london.app.di

import android.content.Context
import android.util.Log
import com.london.data.BuildConfig
import com.london.data.datasource.remote.details.actordetails.ActorDetailsRemoteDataSource
import com.london.data.datasource.remote.details.actordetails.ActorDetailsRemoteDataSourceImpl
import com.london.data.datasource.remote.details.moviedetails.MovieDetailsRemote
import com.london.data.datasource.remote.details.moviedetails.MovieDetailsRemoteImpl
import com.london.data.datasource.remote.details.tvshowdetails.TvShowDetailsRemoteDataSource
import com.london.data.datasource.remote.details.tvshowdetails.TvShowDetailsRemoteDataSourceImpl
import com.london.data.datasource.remote.search.SearchRemoteDataSource
import com.london.data.datasource.remote.search.SearchRemoteDataSourceImpl
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
class DataSourceModule {
    @Single
    fun provideRemoteDataSource(ktorClient: HttpClient): SearchRemoteDataSource {
        return SearchRemoteDataSourceImpl(ktorClient)
    }

    @Single
    fun provideTvShowDetailsRemoteDataSource(
        ktorClient: HttpClient,
    ): TvShowDetailsRemoteDataSource {
        return TvShowDetailsRemoteDataSourceImpl(ktorClient)
    }

    @Single
    fun provideActorDetailsRemoteDataSource(
        ktorClient: HttpClient,
    ): ActorDetailsRemoteDataSource {
        return ActorDetailsRemoteDataSourceImpl(ktorClient)
    }

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


    @Single
    fun provideMovieRemoteDataSource(ktorClient: HttpClient): MovieDetailsRemote =
        MovieDetailsRemoteImpl(ktorClient)

}