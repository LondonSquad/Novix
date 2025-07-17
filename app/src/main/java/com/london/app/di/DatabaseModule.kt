package com.london.app.di

import android.content.Context
import com.london.data.datasource.local.DatabaseProvider
import com.london.data.datasource.local.LocalDataSource
import com.london.data.datasource.local.NovixDatabase
import com.london.data.datasource.local.dao.GenreInterestDao
import com.london.data.datasource.local.dao.SearchActorsDao
import com.london.data.datasource.local.dao.SearchMoviesDao
import com.london.data.datasource.local.dao.SearchTvShowDao
import com.london.data.datasource.local.dao.recent.search.RecentSearchDao
import com.london.data.datasource.local.search.ActorLocalDataSourceImpl
import com.london.data.datasource.local.search.MovieLocalDataSourceImpl
import com.london.data.datasource.local.search.TvShowLocalDataSourceImpl
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.local.model.recent.RecentSearchLocal
import com.london.data.datasource.local.recent.RecentDataSource
import com.london.data.datasource.local.recent.RecentSearchDataSourceImpl
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
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

    fun provideRecentSearchDao(database: NovixDatabase): RecentSearchDao {
        return database.recentSearchDao()
    }

    @Single
    fun provideGenreInterestDao(database: NovixDatabase): GenreInterestDao {
        return database.genreInterestDao()
    }

    @Single
    fun provideRecentViewedDao(database: NovixDatabase): RecentSearchDao {
        return database.recentSearchDao()
    }

    @Single
    @Named("tvShow")
    fun provideTvShowLocalDataSource(
        searchTvShowDao: SearchTvShowDao,
    ): LocalDataSource<SearchTvShowLocal> {
        return TvShowLocalDataSourceImpl(
            searchTvShowDao = searchTvShowDao,
        )
    }

    @Single
    @Named("movie")
    fun provideMovieLocalDataSource(
        searchMoviesDao: SearchMoviesDao,
    ): LocalDataSource<SearchMoviesLocal> {
        return MovieLocalDataSourceImpl(
            searchMoviesDao = searchMoviesDao,
        )
    }

    @Single
    @Named("actor")
    fun provideActorLocalDataSource(
        searchActorsDao: SearchActorsDao,
    ): LocalDataSource<SearchActorsLocal> {
        return ActorLocalDataSourceImpl(
            searchActorsDao = searchActorsDao,
        )
    }

    @Single
    fun provideRecentSearchDataSource(
        recentSearchDao: RecentSearchDao,
    ): RecentDataSource<RecentSearchLocal> {
        return RecentSearchDataSourceImpl(
            recentSearchDao = recentSearchDao,
        )
    }
}