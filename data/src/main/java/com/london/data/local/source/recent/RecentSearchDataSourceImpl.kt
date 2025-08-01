package com.london.data.local.source.recent

import com.london.data.local.database.dao.recent.search.RecentSearchDao
import com.london.data.local.model.recent.search.RecentSearchLocal

import javax.inject.Inject


class RecentSearchDataSourceImpl @Inject constructor(
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
   override suspend fun delete(item: RecentSearchLocal) {
        runCatching {
            recentSearchDao.delete(item)
        }
    }
}
