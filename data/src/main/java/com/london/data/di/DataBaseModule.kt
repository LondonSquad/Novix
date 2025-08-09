package com.london.data.di

import android.content.Context
import androidx.room.Room
import com.london.data.local.database.NovixDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideNovixDatabase(@ApplicationContext context: Context): NovixDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            NovixDatabase::class.java,
            "NovixDatabase"
        ).build()

    @Provides
    @Singleton
    fun provideRecentViewedDao(database: NovixDatabase) =
        database.recentViewedDao()

    @Provides
    @Singleton
    fun provideRecentSearchDao(database: NovixDatabase) =
        database.recentSearchDao()

    @Provides
    @Singleton
    fun provideRecentWatchedMoviesDao(database: NovixDatabase) =
        database.recentWatchedMoviesDao()

    @Provides
    @Singleton
    fun provideRecentWatchedTvShowsDao(database: NovixDatabase) =
        database.recentWatchedTvShowsDao()

    @Provides
    @Singleton
    @Named("popularSectionDao")
    fun providePopularSectionDao(database: NovixDatabase) =
        database.popularSectionDao()

    @Provides
    @Singleton
    @Named("topRatedDao")
    fun provideTopRatedDao(database: NovixDatabase) =
        database.topRatedDao()

    @Provides
    @Singleton
    fun provideUpComingMoviesDao(database: NovixDatabase) =
        database.upComingSectionDao()
}
