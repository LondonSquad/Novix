package com.london.data.di

import android.content.Context
import com.london.data.local.database.DatabaseProvider
import com.london.data.local.database.NovixDatabase
import com.london.data.local.database.dao.recent.search.RecentSearchDao
import com.london.data.local.database.dao.recent.viewed.RecentViewedDao
import com.london.data.local.database.dao.recent.whatched.movie.RecentWatchedMoviesDao
import com.london.data.local.database.dao.recent.whatched.tvshow.RecentWatchedTvShowsDao
import com.london.data.local.database.dao.search.GenreInterestDao
import com.london.data.local.database.dao.search.SearchActorsDao
import com.london.data.local.database.dao.search.SearchMoviesDao
import com.london.data.local.database.dao.search.SearchTvShowDao
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
class DataBaseModule {
    @Single
    fun provideNovixDatabase(context: Context): NovixDatabase {
        return DatabaseProvider.getDatabase(context)
    }

    @Single
    fun provideSearchMoviesDao(database: NovixDatabase): SearchMoviesDao =
        database.searchMoviesDao()


    @Single
    fun provideSearchTvShowDao(database: NovixDatabase): SearchTvShowDao =
        database.searchTvShowDao()


    @Single
    fun provideSearchActorsDao(database: NovixDatabase): SearchActorsDao =
        database.searchActorsDao()


    @Single
    fun provideGenreInterestDao(database: NovixDatabase): GenreInterestDao {
        return database.genreInterestDao()
    }

    @Named("recentViewedDao")
    @Single
    fun provideRecentViewedDao(database: NovixDatabase): RecentViewedDao {
        return database.recentViewedDao()
    }

    @Named("recentSearchDao")
    @Single
    fun provideRecentSearchDao(database: NovixDatabase): RecentSearchDao {
        return database.recentSearchDao()
    }

    @Named("recentWatchedMoviesDao")
    @Single
    fun provideRecentWatchedMoviesDao(database: NovixDatabase): RecentWatchedMoviesDao {
        return database.recentWatchedMoviesDao()
    }

    @Named("recentWatchedTvShowsDao")
    @Single
    fun provideRecentWatchedTvShowsDao(database: NovixDatabase): RecentWatchedTvShowsDao {
        return database.recentWatchedTvShowsDao()
    }
}
