package com.london.data.di

import com.london.data.remote.service.account.AccountApiService
import com.london.data.remote.service.actor.ActorDetailsApiService
import com.london.data.remote.service.authentication.AuthenticationApiService
import com.london.data.remote.service.list.CustomMovieListsApiService
import com.london.data.remote.service.movie.MovieService
import com.london.data.remote.service.search.SearchApiService
import com.london.data.remote.service.tvshow.TvShowDetailsApiService
import com.london.data.remote.source.account.AccountRemoteDataSource
import com.london.data.remote.source.account.AccountRemoteDataSourceImp
import com.london.data.remote.source.actor.ActorDetailsRemoteDataSource
import com.london.data.remote.source.actor.ActorDetailsRemoteDataSourceImpl
import com.london.data.remote.source.authentication.AuthenticationRemoteDataSource
import com.london.data.remote.source.authentication.AuthenticationRemoteDataSourceImpl
import com.london.data.remote.source.list.CustomMovieListsRemoteDataSource
import com.london.data.remote.source.list.CustomMovieListsRemoteDataSourceImpl
import com.london.data.remote.source.movie.MovieRemoteDataSource
import com.london.data.remote.source.movie.MovieRemoteDataSourceImpl
import com.london.data.remote.source.search.SearchRemoteDataSource
import com.london.data.remote.source.search.SearchRemoteDataSourceImpl
import com.london.data.remote.source.tvshow.TvShowRemoteDataSource
import com.london.data.remote.source.tvshow.TvShowRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Provides
    @Singleton
    fun provideActorDetailsRemoteDataSource(
        apiService: ActorDetailsApiService,
    ): ActorDetailsRemoteDataSource =
        ActorDetailsRemoteDataSourceImpl(actorDetailsApiService = apiService)

    @Provides
    @Singleton
    fun provideTvShowDetailsRemoteDataSource(
        apiService: TvShowDetailsApiService,
    ): TvShowRemoteDataSource =
        TvShowRemoteDataSourceImpl(tvShowDetailsApiService = apiService)

    @Provides
    @Singleton
    fun provideSearchRemoteDataSource(
        apiService: SearchApiService,
    ): SearchRemoteDataSource = SearchRemoteDataSourceImpl(searchApiService = apiService)

    @Provides
    @Singleton
    fun provideAuthenticationRemoteDataSource(
        apiService: AuthenticationApiService,
    ): AuthenticationRemoteDataSource =
        AuthenticationRemoteDataSourceImpl(authApiService = apiService)

    @Provides
    @Singleton
    fun provideMovieDetailsRemoteDataSource(
        apiService: MovieService,
    ): MovieRemoteDataSource =
        MovieRemoteDataSourceImpl(movieService = apiService)

    @Provides
    @Singleton
    fun provideCustomMovieListsRemoteDataSource(
        apiService: CustomMovieListsApiService,
    ): CustomMovieListsRemoteDataSource =
        CustomMovieListsRemoteDataSourceImpl(customMovieListsApiService = apiService)

    @Provides
    @Singleton
    fun provideAccountRemoteDataSource(
        accountApiService: AccountApiService
    ): AccountRemoteDataSource =
        AccountRemoteDataSourceImp(accountApiService = accountApiService)
}
