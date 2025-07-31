package com.london.data.di

import android.content.Context
import com.london.data.local.database.DatabaseProvider
import com.london.data.local.database.NovixDatabase
import com.london.data.local.database.dao.search.SearchActorsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideNovixDatabase(@ApplicationContext context: Context) =
        DatabaseProvider.getDatabase(context = context)

    @Provides
    fun provideSearchMoviesDao(database: NovixDatabase) =
        database.searchMoviesDao()

    @Provides
    fun provideSearchTvShowDao(database: NovixDatabase) =
        database.searchTvShowDao()

    @Provides
    fun provideSearchActorsDao(database: NovixDatabase): SearchActorsDao =
        database.searchActorsDao()

    @Provides
    fun provideGenreInterestDao(database: NovixDatabase) =
        database.genreInterestDao()

    @Provides
    fun provideRecentViewedDao(database: NovixDatabase) =
        database.recentViewedDao()

    @Provides
    fun provideRecentSearchDao(database: NovixDatabase) =
        database.recentSearchDao()

    @Provides
    fun provideRecentWatchedMoviesDao(database: NovixDatabase) =
        database.recentWatchedMoviesDao()

    @Provides
    fun provideRecentWatchedTvShowsDao(database: NovixDatabase) =
        database.recentWatchedTvShowsDao()

}
