package com.london.data.di

import android.content.Context
import com.london.data.datasource.local.DatabaseProvider
import com.london.data.datasource.local.NovixDatabase
import com.london.data.datasource.local.dao.recent.search.RecentSearchDao
import com.london.data.datasource.local.dao.recent.viewed.RecentViewedDao
import com.london.data.datasource.local.dao.search.GenreInterestDao
import com.london.data.datasource.local.dao.search.SearchActorsDao
import com.london.data.datasource.local.dao.search.SearchMoviesDao
import com.london.data.datasource.local.dao.search.SearchTvShowDao
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
    fun provideSearchMoviesDao(database: NovixDatabase): SearchMoviesDao {
        return database.searchMoviesDao()
    }

    @Single
    fun provideSearchTvShowDao(database: NovixDatabase): SearchTvShowDao {
        return database.searchTvShowDao()
    }

    @Single
    fun provideSearchActorsDao(database: NovixDatabase): SearchActorsDao {
        return database.searchActorsDao()
    }

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
}