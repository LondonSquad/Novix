package com.london.app.di

import com.london.data.datasource.device.DeviceConfigurationDataSource
import com.london.data.datasource.remote.details.actordetails.ActorDetailsRemoteDataSource
import com.london.data.datasource.remote.details.actordetails.ActorDetailsRemoteDataSourceImpl
import com.london.data.datasource.remote.details.tvshowdetails.TvShowDetailsRemoteDataSource
import com.london.data.datasource.remote.details.tvshowdetails.TvShowDetailsRemoteDataSourceImpl
import com.london.data.datasource.remote.search.RemoteDataSource
import com.london.data.datasource.remote.search.SearchRemoteDataSourceImpl
import io.ktor.client.HttpClient
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DataSourceModule {
    @Single
    fun provideRemoteDataSource(ktorClient: HttpClient): RemoteDataSource {
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
        return HttpClient()
    }
}