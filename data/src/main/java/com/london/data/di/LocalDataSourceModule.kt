package com.london.data.di

import android.content.SharedPreferences
import com.london.data.local.database.dao.home.popular.PopularSectionDao
import com.london.data.local.database.dao.home.toprated.TopRatedDao
import com.london.data.local.database.dao.home.upcoming.UpcomingSectionDao
import com.london.data.local.database.dao.recent.search.RecentSearchDao
import com.london.data.local.database.dao.recent.viewed.RecentViewedDao
import com.london.data.local.database.dao.recent.whatched.movie.RecentWatchedMoviesDao
import com.london.data.local.database.dao.recent.whatched.tvshow.RecentWatchedTvShowsDao
import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.local.model.recent.search.RecentSearchLocal
import com.london.data.local.model.recent.viewed.RecentViewedLocal
import com.london.data.local.model.recent.watched.RecentWatchedMovieLocal
import com.london.data.local.model.recent.watched.RecentWatchedTvShowLocal
import com.london.data.local.preference.AppPreferencesServiceImpl
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {
    @Provides
    @Singleton
    fun provideRecentWatchedTvShowsDataSource(
        dao: RecentWatchedTvShowsDao
    ): RecentWatchedDataSource<RecentWatchedTvShowLocal> =
        RecentWatchedTvShowsDataSource(recentWatchedTvShowsDao = dao)

    @Provides
    @Singleton
    fun provideRecentWatchedMovieDataSource(
        dao: RecentWatchedMoviesDao
    ): RecentWatchedDataSource<RecentWatchedMovieLocal> =
        RecentWatchedMoviesDataSource(recentWatchedMoviesDao = dao)


    @Provides
    @Singleton
    fun provideRecentSearchDataSource(
        dao: RecentSearchDao
    ): RecentDataSource<RecentSearchLocal> = RecentSearchDataSourceImpl(recentSearchDao = dao)

    @Provides
    @Singleton
    fun provideRecentViewedDataSource(
        dao: RecentViewedDao
    ): RecentDataSource<RecentViewedLocal> = RecentViewedDataSourceImpl(recentViewedDao = dao)

    @Provides
    @Singleton
    fun provideAppPreferencesService(
        sharedPreferences: SharedPreferences
    ): AppPreferencesService = AppPreferencesServiceImpl(preferences = sharedPreferences)

    @Provides
    @Singleton
    @Named("popularLocalDataSource")
    fun providePopularLocalDataSource(
        @Named("popularSectionDao") popularSectionDao: PopularSectionDao
    ): HomeLocalDataSource<PopularSectionLocal> =
        PopularLocalDataSourceImpl(popularSectionDao)

    @Provides
    @Singleton
    fun provideUpComingLocalDataSource(
        upComingSectionDao: UpcomingSectionDao
    ): UpComingLocalDataSource = UpComingLocalDataSourceImpl(upComingSectionDao)

    @Provides
    @Singleton
    @Named("topRatedLocalDataSource")
    fun provideTopRatedLocalDataSource(
        @Named("topRatedDao") topRatedDao: TopRatedDao
    ): HomeLocalDataSource<TopRatedLocal> =
        TopRatedDataSourceImpl(topRatedDao)
}
