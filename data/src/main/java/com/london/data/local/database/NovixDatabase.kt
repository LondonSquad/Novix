package com.london.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.london.data.local.database.convertor.CommonConverter
import com.london.data.local.database.convertor.RecentViewedConverter
import com.london.data.local.database.convertor.SearchActorsConvertor
import com.london.data.local.database.convertor.SearchMoviesConverter
import com.london.data.local.database.convertor.SearchTvShowConvertor
import com.london.data.local.database.dao.recent.search.RecentSearchDao
import com.london.data.local.database.dao.recent.viewed.RecentViewedDao
import com.london.data.local.database.dao.recent.whatched.movie.RecentWatchedMoviesDao
import com.london.data.local.database.dao.recent.whatched.tvshow.RecentWatchedTvShowsDao
import com.london.data.local.database.dao.search.GenreInterestDao
import com.london.data.local.database.dao.search.SearchActorsDao
import com.london.data.local.database.dao.search.SearchMoviesDao
import com.london.data.local.database.dao.search.SearchTvShowDao
import com.london.data.local.model.recent.search.RecentSearchLocal
import com.london.data.local.model.recent.viewed.RecentViewedLocal
import com.london.data.local.model.recent.watched.RecentWatchedMovieLocal
import com.london.data.local.model.recent.watched.RecentWatchedTvShowLocal
import com.london.data.local.model.search.GenreInterestEntity
import com.london.data.local.model.search.SearchActorsLocal
import com.london.data.local.model.search.SearchMoviesLocal
import com.london.data.local.model.search.SearchTvShowLocal

@Database(
    entities = [
        SearchTvShowLocal::class,
        SearchMoviesLocal::class,
        SearchActorsLocal::class,
        RecentSearchLocal::class,
        GenreInterestEntity::class,
        RecentViewedLocal::class,
        RecentWatchedMovieLocal::class,
        RecentWatchedTvShowLocal::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    SearchActorsConvertor::class,
    SearchMoviesConverter::class,
    SearchTvShowConvertor::class,
    CommonConverter::class,
    RecentViewedConverter::class,
)
abstract class NovixDatabase : RoomDatabase() {
    abstract fun searchTvShowDao(): SearchTvShowDao
    abstract fun searchMoviesDao(): SearchMoviesDao
    abstract fun searchActorsDao(): SearchActorsDao
    abstract fun recentSearchDao(): RecentSearchDao
    abstract fun genreInterestDao(): GenreInterestDao
    abstract fun recentViewedDao(): RecentViewedDao
    abstract fun recentWatchedMoviesDao(): RecentWatchedMoviesDao
    abstract fun recentWatchedTvShowsDao(): RecentWatchedTvShowsDao
}
