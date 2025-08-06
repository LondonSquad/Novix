package com.london.data.local.database.dao.recent.watched.tvshow

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.london.data.local.database.dao.recent.watched.RecentWatchedDao
import com.london.data.local.model.recent.watched.RecentWatchedTvShowLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentWatchedTvShowsDao : RecentWatchedDao<RecentWatchedTvShowLocal> {

    @Query("SELECT * FROM recent_watched_tv_show_table")
    override fun getAll(): Flow<List<RecentWatchedTvShowLocal>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    override suspend fun insert(item: RecentWatchedTvShowLocal)
}
