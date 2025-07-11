package com.london.app.di

import android.content.Context
import com.london.data.datasource.local.DatabaseProvider
import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.LocalDataSourceImpl
import com.london.data.datasource.local.NovixDatabase
import com.london.data.datasource.local.dao.SearchActorsDao
import com.london.data.datasource.local.dao.SearchMoviesDao
import com.london.data.datasource.local.dao.SearchTvShowDao
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseModule {
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
    fun provideLocalDataSource(
        searchTvShowDao: SearchTvShowDao,
        searchMoviesDao: SearchMoviesDao,
        searchActorsDao: SearchActorsDao
    ): LocalDataSource {
        return LocalDataSourceImpl(
            searchTvShowDao = searchTvShowDao,
            searchMoviesDao = searchMoviesDao,
            searchActorsDao = searchActorsDao
        )
    }
}