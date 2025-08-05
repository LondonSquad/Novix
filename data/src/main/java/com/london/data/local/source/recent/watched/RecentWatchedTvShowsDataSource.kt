package com.london.data.local.source.recent.watched

import com.london.data.local.database.dao.recent.watched.tvshow.RecentWatchedTvShowsDao
import com.london.data.local.model.recent.watched.RecentWatchedTvShowLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class RecentWatchedTvShowsDataSource @Inject constructor(
    private val recentWatchedTvShowsDao: RecentWatchedTvShowsDao
) : RecentWatchedDataSource<RecentWatchedTvShowLocal> {

    override suspend fun getAll(): Flow<List<RecentWatchedTvShowLocal>> =
        recentWatchedTvShowsDao.getAll().catch { emit(emptyList()) }.distinctUntilChanged()

    override suspend fun insert(item: RecentWatchedTvShowLocal) {
        runCatching {
            recentWatchedTvShowsDao.insert(item)
        }
    }
}
