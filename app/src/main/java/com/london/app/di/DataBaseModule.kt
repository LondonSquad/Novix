package com.london.app.di

import android.content.Context
import androidx.room.Room
import com.london.data.local.database.NovixDatabase
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
    fun provideNovixDatabase(@ApplicationContext context: Context): NovixDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            NovixDatabase::class.java,
            "NovixDatabase"
        ).build()

    @Provides
    @Singleton
    fun provideRecentViewedDao(database: NovixDatabase) = database.recentViewedDao()

    @Provides
    @Singleton
    fun provideRecentSearchDao(database: NovixDatabase) = database.recentSearchDao()

    @Provides
    @Singleton
    fun provideRecentWatchedMoviesDao(database: NovixDatabase) = database.recentWatchedMoviesDao()

    @Provides
    @Singleton
    fun provideRecentWatchedTvShowsDao(database: NovixDatabase) = database.recentWatchedTvShowsDao()

    @Provides
    @Singleton
    fun providePopularSectionDao(database: NovixDatabase) = database.popularSectionDao()

    @Provides
    @Singleton
    fun provideTopRatedDao(database: NovixDatabase) = database.topRatedDao()

    @Provides
    @Singleton
    fun provideUpComingMoviesDao(database: NovixDatabase) = database.upComingSectionDao()

    @Provides
    @Singleton
    fun provideGenreInterestDao(database: NovixDatabase) = database.genreInterestDao()

    @Provides
    @Singleton
    fun provideMovieListDao(database: NovixDatabase) = database.movieListDao()

    @Provides
    @Singleton
    fun provideMovieListMembershipDao(database: NovixDatabase) = database.listMembershipDao()

    @Provides
    @Singleton
    fun provideSyncMetadataDao(database: NovixDatabase) = database.syncMetadataDao()
}
