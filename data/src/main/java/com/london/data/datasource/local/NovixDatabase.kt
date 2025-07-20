package com.london.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.london.data.datasource.local.convertor.CommonConverter
import com.london.data.datasource.local.convertor.RecentViewedConverter
import com.london.data.datasource.local.convertor.SearchActorsConvertor
import com.london.data.datasource.local.convertor.SearchMoviesConverter
import com.london.data.datasource.local.convertor.SearchTvShowConvertor
import com.london.data.datasource.local.dao.recent.search.RecentSearchDao
import com.london.data.datasource.local.dao.recent.viewed.RecentViewedDao
import com.london.data.datasource.local.dao.search.GenreInterestDao
import com.london.data.datasource.local.dao.search.SearchActorsDao
import com.london.data.datasource.local.dao.search.SearchMoviesDao
import com.london.data.datasource.local.dao.search.SearchTvShowDao
import com.london.data.datasource.local.model.GenreInterestEntity
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.local.model.recent.RecentSearchLocal
import com.london.data.datasource.local.model.recent.RecentViewedLocal

@Database(
    entities = [
        SearchTvShowLocal::class,
        SearchMoviesLocal::class,
        SearchActorsLocal::class,
        RecentSearchLocal::class,
        GenreInterestEntity::class,
        RecentViewedLocal::class,
    ],
    version = 1,
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
}
