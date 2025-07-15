package com.london.data.datasource.local.recentsearch

import com.london.data.datasource.local.dao.recentsearch.RecentSearchDao
import com.london.data.datasource.local.model.RecentSearch

class RecentSearchDataSourceImpl(
    private val recentSearchDao: RecentSearchDao
) : RecentSearchDataSource {
    override suspend fun insert(item: RecentSearch) {
        runCatching {
            recentSearchDao.insert(item)
        }
    }

    override suspend fun clearOlderThanTen() {
        runCatching {
            recentSearchDao.clearOlderThanTen()
        }
    }

    override suspend fun getAll(): List<RecentSearch> = runCatching {
        recentSearchDao.getAll()
    }.getOrDefault(emptyList())

    override suspend fun getRecentTen(): List<RecentSearch> = runCatching {
        recentSearchDao.getRecentTen()
    }.getOrDefault(emptyList())

    override suspend fun insertAndKeepLastTen(item: RecentSearch) {
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