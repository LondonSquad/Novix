package com.london.app.di

import android.util.Log
import com.london.data.datasource.device.DeviceConfigurationDataSource
import com.london.data.datasource.remote.details.moviedetails.MovieDetailsRemote
import com.london.data.datasource.remote.details.actordetails.ActorDetailsRemoteDataSource
import com.london.data.datasource.remote.details.actordetails.ActorDetailsRemoteDataSourceImpl
import com.london.data.datasource.remote.details.tvshowdetails.TvShowDetailsRemoteDataSource
import com.london.data.datasource.remote.details.tvshowdetails.TvShowDetailsRemoteDataSourceImpl
import com.london.data.datasource.remote.search.SearchRemoteDataSource
import com.london.data.datasource.remote.search.SearchRemoteDataSourceImpl
import com.london.data.datasource.util.MovieDetailsRemoteImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
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
        deviceConfigurationDataSource: DeviceConfigurationDataSource
    ): TvShowDetailsRemoteDataSource {
        return TvShowDetailsRemoteDataSourceImpl(ktorClient, deviceConfigurationDataSource)
    }

    @Single
    fun provideActorDetailsRemoteDataSource(
        ktorClient: HttpClient,
        deviceConfigurationDataSource: DeviceConfigurationDataSource
    ): ActorDetailsRemoteDataSource {
        return ActorDetailsRemoteDataSourceImpl(ktorClient, deviceConfigurationDataSource)
    }

    @Single
    fun provideKtorClient(): HttpClient {
        return HttpClient {
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

    @Single
    fun provideMovieRemoteDataSource(ktorClient: HttpClient): MovieDetailsRemote =
        MovieDetailsRemoteImpl(ktorClient)

}