package com.london.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.london.data.datasource.local.convertor.CommonConverter
import com.london.data.datasource.local.convertor.SearchActorsConvertor
import com.london.data.datasource.local.convertor.SearchMoviesConverter
import com.london.data.datasource.local.convertor.SearchTvShowConvertor
import com.london.data.datasource.local.dao.SearchActorsDao
import com.london.data.datasource.local.dao.SearchMoviesDao
import com.london.data.datasource.local.dao.SearchTvShowDao
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal

@Database(
    entities = [
        SearchTvShowLocal::class,
        SearchMoviesLocal::class,
        SearchActorsLocal::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    SearchActorsConvertor::class,
    SearchMoviesConverter::class,
    SearchTvShowConvertor::class,
    CommonConverter::class
)
abstract class NovixDatabase : RoomDatabase() {
    abstract fun searchTvShowDao(): SearchTvShowDao
    abstract fun searchMoviesDao(): SearchMoviesDao
    abstract fun searchActorsDao(): SearchActorsDao
}