package com.london.data.datasource.local.recent

import com.london.data.datasource.local.dao.recent.viewed.RecentViewedDao
import com.london.data.datasource.local.model.recent.RecentViewedLocal
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
@Named("recentViewedDataSource")
class RecentViewedDataSourceImpl(
    @Provided
@Named("recentViewedDao")
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
}
