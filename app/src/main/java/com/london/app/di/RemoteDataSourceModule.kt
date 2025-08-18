package com.london.app.di

import com.london.data.remote.source.account.AccountRemoteDataSource
import com.london.data.remote.source.account.AccountRemoteDataSourceImp
import com.london.data.remote.source.actor.ActorRemoteDataSource
import com.london.data.remote.source.actor.ActorRemoteDataSourceImpl
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
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    @Singleton
    abstract fun provideActorDetailsRemoteDataSource(
        implementation: ActorRemoteDataSourceImpl
    ): ActorRemoteDataSource

    @Binds
    @Singleton
    abstract fun provideTvShowDetailsRemoteDataSource(
        implementation: TvShowRemoteDataSourceImpl
    ): TvShowRemoteDataSource

    @Binds
    @Singleton
    abstract fun provideSearchRemoteDataSource(
        implementation: SearchRemoteDataSourceImpl
    ): SearchRemoteDataSource

    @Binds
    @Singleton
    abstract fun provideAuthenticationRemoteDataSource(
        implementation: AuthenticationRemoteDataSourceImpl
    ): AuthenticationRemoteDataSource

    @Binds
    @Singleton
    abstract fun provideMovieDetailsRemoteDataSource(
        implementation: MovieRemoteDataSourceImpl
    ): MovieRemoteDataSource

    @Binds
    @Singleton
    abstract fun provideCustomMovieListsRemoteDataSource(
        implementation: CustomMovieListsRemoteDataSourceImpl
    ): CustomMovieListsRemoteDataSource

    @Binds
    @Singleton
    abstract fun provideAccountRemoteDataSource(
        implementation: AccountRemoteDataSourceImp
    ): AccountRemoteDataSource
}
