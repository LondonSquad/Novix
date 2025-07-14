package com.london.app.di

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
    fun provideRemoteDataSource(ktorClient: HttpClient): RemoteDataSource{
        return SearchRemoteDataSourceImpl(ktorClient)
    }

    @Single
    fun provideDetailsRemoteDataSource(ktorClient: HttpClient): TvShowDetailsRemoteDataSource {
        return TvShowDetailsRemoteDataSourceImpl(ktorClient)
    }

    @Single
    fun provideKtorClient(): HttpClient{
        return HttpClient()
    }
}