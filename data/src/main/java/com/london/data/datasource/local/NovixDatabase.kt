package com.london.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.london.data.datasource.local.convertor.CommonConverter
import com.london.data.datasource.local.convertor.SearchActorsResponseConvertor
import com.london.data.datasource.local.convertor.SearchMoviesResponseConverter
import com.london.data.datasource.local.convertor.SearchTvShowsResponseConvertor
import com.london.data.datasource.local.dao.SearchActorsDao
import com.london.data.datasource.local.dao.SearchMoviesDao
import com.london.data.datasource.local.dao.SearchTvShowDao
import com.london.data.datasource.local.dto.SearchActorsResponse
import com.london.data.datasource.local.dto.SearchMoviesResponseLocal
import com.london.data.datasource.local.dto.SearchTvShowsResponseLocal

@Database(
    entities = [SearchTvShowsResponseLocal::class, SearchMoviesResponseLocal::class, SearchActorsResponse::class],
    version = 1
)
@TypeConverters(
    SearchActorsResponseConvertor::class,
    SearchMoviesResponseConverter::class,
    SearchTvShowsResponseConvertor::class,
    CommonConverter::class
)
abstract class NovixDatabase : RoomDatabase() {
    abstract fun searchTvShowDao(): SearchTvShowDao
    abstract fun searchMoviesDao(): SearchMoviesDao
    abstract fun searchActorsDao(): SearchActorsDao
}