package com.london.data.datasource.local.dao.recent.whatched.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.london.data.datasource.local.dao.recent.whatched.RecentWatchedDao
import com.london.data.datasource.local.model.recent.watched.RecentWatchedMovieLocal

@Dao
interface RecentWatchedMoviesDao: RecentWatchedDao<RecentWatchedMovieLocal>{

    @Query("SELECT * FROM recent_watched_movie_table")
    override suspend fun getAll(): List<RecentWatchedMovieLocal>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    override suspend fun insert(item: RecentWatchedMovieLocal)
}