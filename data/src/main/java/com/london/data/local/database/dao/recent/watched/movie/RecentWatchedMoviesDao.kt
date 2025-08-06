@file:KoverIgnore

package com.london.data.local.database.dao.recent.watched.movie

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.london.data.local.database.dao.recent.watched.RecentWatchedDao
import com.london.data.local.model.recent.watched.RecentWatchedMovieLocal
import com.london.domain.KoverIgnore
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentWatchedMoviesDao : RecentWatchedDao<RecentWatchedMovieLocal> {

    @Query("SELECT * FROM recent_watched_movie_table")
    override fun getAll(): Flow<List<RecentWatchedMovieLocal>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    override suspend fun insert(item: RecentWatchedMovieLocal)
}
