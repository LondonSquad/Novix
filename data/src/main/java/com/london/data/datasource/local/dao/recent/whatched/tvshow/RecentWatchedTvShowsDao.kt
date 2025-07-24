package com.london.data.datasource.local.dao.recent.whatched.tvshow

import androidx.room.Dao
import com.london.data.datasource.local.dao.recent.whatched.RecentWatchedDao
import com.london.data.datasource.local.model.recent.watched.RecentWatchedTvShowLocal

@Dao
interface RecentWatchedTvShowsDao: RecentWatchedDao<RecentWatchedTvShowLocal>{
    override suspend fun getAll(): List<RecentWatchedTvShowLocal>
    override suspend fun insert(item: RecentWatchedTvShowLocal)
}