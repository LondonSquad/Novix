package com.london.data.datasource.local.recentsearch

import com.london.data.datasource.local.model.RecentSearch

interface RecentSearchDataSource {
    suspend fun insert(item: RecentSearch)
    suspend fun clearOlderThanTen()
    suspend fun getAll(): List<RecentSearch>
    suspend fun getRecentTen(): List<RecentSearch>
    suspend fun insertAndKeepLastTen(item: RecentSearch)
    suspend fun clearAll()
}