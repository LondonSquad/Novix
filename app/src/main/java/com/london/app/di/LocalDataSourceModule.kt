package com.london.app.di

import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.local.model.recent.search.RecentSearchLocal
import com.london.data.local.model.recent.viewed.RecentViewedLocal
import com.london.data.local.model.recent.watched.RecentWatchedMovieLocal
import com.london.data.local.model.recent.watched.RecentWatchedTvShowLocal
import com.london.data.local.preference.AppPreferencesServiceImpl
import com.london.data.local.source.customLists.CustomMovieListLocalDataSource
import com.london.data.local.source.customLists.CustomMovieListLocalDataSourceImpl
import com.london.data.local.source.home.HomeLocalDataSource
import com.london.data.local.source.home.popular.PopularLocalDataSourceImpl
import com.london.data.local.source.home.toprated.TopRatedDataSourceImpl
import com.london.data.local.source.home.upcoming.UpComingLocalDataSource
import com.london.data.local.source.home.upcoming.UpComingLocalDataSourceImpl
import com.london.data.local.source.recent.RecentDataSource
import com.london.data.local.source.recent.RecentSearchDataSourceImpl
import com.london.data.local.source.recent.RecentViewedDataSourceImpl
import com.london.data.local.source.recent.watched.RecentWatchedDataSource
import com.london.data.local.source.recent.watched.RecentWatchedMoviesDataSource
import com.london.data.local.source.recent.watched.RecentWatchedTvShowsDataSource
import com.london.domain.AppPreferencesService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    @Singleton
    abstract fun provideRecentWatchedTvShowsDataSource(
        implementation: RecentWatchedTvShowsDataSource
    ): RecentWatchedDataSource<RecentWatchedTvShowLocal>

    @Binds
    @Singleton
    abstract fun provideRecentWatchedMovieDataSource(
        implementation: RecentWatchedMoviesDataSource
    ): RecentWatchedDataSource<RecentWatchedMovieLocal>


    @Binds
    @Singleton
    abstract fun provideRecentSearchDataSource(
        implementation: RecentSearchDataSourceImpl
    ): RecentDataSource<RecentSearchLocal>


    @Binds
    @Singleton
    abstract fun provideRecentViewedDataSource(
        implementation: RecentViewedDataSourceImpl
    ): RecentDataSource<RecentViewedLocal>

    @Binds
    @Singleton
    abstract fun provideAppPreferencesService(
        implementation: AppPreferencesServiceImpl
    ): AppPreferencesService

    @Binds
    @Singleton
    abstract fun providePopularLocalDataSource(
        implementation: PopularLocalDataSourceImpl
    ): HomeLocalDataSource<PopularSectionLocal>

    @Binds
    @Singleton
    abstract fun provideUpComingLocalDataSource(
        implementation: UpComingLocalDataSourceImpl
    ): UpComingLocalDataSource

    @Binds
    @Singleton
    abstract fun provideTopRatedLocalDataSource(
        implementation: TopRatedDataSourceImpl
    ): HomeLocalDataSource<TopRatedLocal>

    @Binds
    @Singleton
    abstract fun provideCustomMovieListLocalDataSource(
        implementation: CustomMovieListLocalDataSourceImpl
    ): CustomMovieListLocalDataSource
}
