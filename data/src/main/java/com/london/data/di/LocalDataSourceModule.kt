package com.london.data.di

import android.content.SharedPreferences
import com.london.data.local.database.dao.recent.search.RecentSearchDao
import com.london.data.local.database.dao.recent.viewed.RecentViewedDao
import com.london.data.local.database.dao.recent.whatched.movie.RecentWatchedMoviesDao
import com.london.data.local.database.dao.recent.whatched.tvshow.RecentWatchedTvShowsDao
import com.london.data.local.database.dao.search.SearchActorsDao
import com.london.data.local.database.dao.search.SearchMoviesDao
import com.london.data.local.database.dao.search.SearchTvShowDao
import com.london.data.local.preference.AppPreferencesServiceImpl
import com.london.data.local.source.recent.RecentSearchDataSourceImpl
import com.london.data.local.source.recent.RecentViewedDataSourceImpl
import com.london.data.local.source.recent.watched.RecentWatchedMoviesDataSource
import com.london.data.local.source.recent.watched.RecentWatchedTvShowsDataSource
import com.london.data.local.source.search.ActorLocalDataSourceImpl
import com.london.data.local.source.search.MovieLocalDataSourceImpl
import com.london.data.local.source.search.TvShowLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun provideRecentWatchedTvShowsDataSource(
        dao: RecentWatchedTvShowsDao
    ) = RecentWatchedTvShowsDataSource(recentWatchedTvShowsDao = dao)

    @Provides
    fun provideRecentWatchedMovieDataSource(
        dao: RecentWatchedMoviesDao
    ) = RecentWatchedMoviesDataSource(recentWatchedMoviesDao = dao)

    @Provides
    @Singleton
    fun provideTvShowLocalDataSource(
        dao: SearchTvShowDao
    ) = TvShowLocalDataSourceImpl(searchTvShowDao = dao)

    @Provides
    fun provideActorLocalDataSource(
        dao: SearchActorsDao
    ) = ActorLocalDataSourceImpl(searchActorsDao = dao)

    @Provides
    fun provideSearchMoviesLocalDataSource(
        dao: SearchMoviesDao
    ) = MovieLocalDataSourceImpl(searchMoviesDao =  dao)

    @Provides
    fun provideRecentSearchDataSource(
        dao: RecentSearchDao
    ) = RecentSearchDataSourceImpl(recentSearchDao = dao)

    @Provides
    fun provideRecentViewedDataSource(
        dao: RecentViewedDao
    ) = RecentViewedDataSourceImpl(recentViewedDao = dao)

    @Provides
    @Singleton
    fun provideAppPreferencesService(
        sharedPreferences: SharedPreferences
    ) = AppPreferencesServiceImpl(preferences = sharedPreferences)
}
