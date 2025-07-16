package com.london.data.datasource.local.recent.search

import com.london.data.datasource.local.dao.recent.search.RecentSearchDao
import com.london.data.datasource.local.model.recent.RecentSearchLocal
import com.london.data.datasource.local.recent.RecentDataSource

class RecentSearchDataSourceImpl(
    private val recentSearchDao: RecentSearchDao
) : RecentDataSource<RecentSearchLocal> {
    override suspend fun insert(item: RecentSearchLocal) {
        runCatching {
            recentSearchDao.insert(item)
        }
    }

    override suspend fun clearOlderThanTen() {
        runCatching {
            recentSearchDao.clearOlderThanTen()
        }
    }

    override suspend fun getAll(): List<RecentSearchLocal> = runCatching {
        recentSearchDao.getAll()
    }.getOrDefault(emptyList())

    override suspend fun getRecentTen(): List<RecentSearchLocal> = runCatching {
        recentSearchDao.getRecentTen()
    }.getOrDefault(emptyList())

    override suspend fun insertAndKeepLastTen(item: RecentSearchLocal) {
        runCatching {
            recentSearchDao.insertAndKeepLastTen(item)
        }
    }

    override suspend fun clearAll() {
        runCatching {
            recentSearchDao.clearAll()
        }
    }
}