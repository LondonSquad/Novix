package com.london.data.local.source.recent.watched

import com.london.data.local.database.dao.recent.whatched.tvshow.RecentWatchedTvShowsDao
import com.london.data.local.model.recent.watched.RecentWatchedTvShowLocal
import javax.inject.Inject

class RecentWatchedTvShowsDataSource @Inject constructor(
    private val recentWatchedTvShowsDao: RecentWatchedTvShowsDao
) : RecentWatchedDataSource<RecentWatchedTvShowLocal> {

    override suspend fun getAll(): List<RecentWatchedTvShowLocal> = runCatching {
        recentWatchedTvShowsDao.getAll()
    }.getOrDefault(emptyList())

    override suspend fun insert(item: RecentWatchedTvShowLocal) {
        runCatching {
            recentWatchedTvShowsDao.insert(item)
        }
    }
}
