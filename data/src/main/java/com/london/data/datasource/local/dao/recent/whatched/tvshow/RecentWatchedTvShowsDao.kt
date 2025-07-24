package com.london.data.datasource.local.dao.recent.whatched.tvshow

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.london.data.datasource.local.dao.recent.whatched.RecentWatchedDao
import com.london.data.datasource.local.model.recent.watched.RecentWatchedTvShowLocal

@Dao
interface RecentWatchedTvShowsDao: RecentWatchedDao<RecentWatchedTvShowLocal>{

    @Query("SELECT * FROM recent_watched_tv_show_table")
    override suspend fun getAll(): List<RecentWatchedTvShowLocal>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    override suspend fun insert(item: RecentWatchedTvShowLocal)
}
