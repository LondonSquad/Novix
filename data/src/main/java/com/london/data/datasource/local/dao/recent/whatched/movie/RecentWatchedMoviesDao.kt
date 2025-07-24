package com.london.data.datasource.local.dao.recent.whatched.movie

import androidx.room.Dao
import com.london.data.datasource.local.dao.recent.whatched.RecentWatchedDao
import com.london.data.datasource.local.model.recent.watched.RecentWatchedMovieLocal

@Dao
interface RecentWatchedMoviesDao: RecentWatchedDao<RecentWatchedMovieLocal>{
    override suspend fun getAll(): List<RecentWatchedMovieLocal>
    override suspend fun insert(item: RecentWatchedMovieLocal)
}