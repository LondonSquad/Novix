package com.london.data.local.source.recent

import com.london.data.local.database.dao.recent.viewed.RecentViewedDao
import com.london.data.local.model.recent.viewed.RecentViewedLocal
import javax.inject.Inject

class RecentViewedDataSourceImpl @Inject constructor(
    private val recentViewedDao: RecentViewedDao
) : RecentDataSource<RecentViewedLocal> {

    override suspend fun insert(item: RecentViewedLocal) {
        runCatching {
            recentViewedDao.insert(item)
        }
    }

    override suspend fun clearOlderThanTen() {
        runCatching {
            recentViewedDao.clearOlderThanTen()
        }
    }

    override suspend fun getAll(): List<RecentViewedLocal> = runCatching {
        recentViewedDao.getAll()
    }.getOrDefault(emptyList())

    override suspend fun getRecentTen(): List<RecentViewedLocal> = runCatching {
        recentViewedDao.getRecentTen()
    }.getOrDefault(emptyList())

    override suspend fun insertAndKeepLastTen(item: RecentViewedLocal) {
        runCatching {
            recentViewedDao.insertAndKeepLastTen(item)
        }
    }

    override suspend fun clearAll() {
        runCatching {
            recentViewedDao.clearAll()
        }
    }

    override suspend fun delete(item: RecentViewedLocal) {
        //TODO("Not yet implemented")
    }
}
