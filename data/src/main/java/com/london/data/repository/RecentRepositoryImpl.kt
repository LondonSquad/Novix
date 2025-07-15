package com.london.data.repository

import com.london.data.datasource.local.recentsearch.RecentSearchDataSource
import com.london.data.mapper.recent.toRecentSearch
import com.london.data.mapper.recent.toStringQuery
import com.london.domain.repository.RecentRepository

class RecentRepositoryImpl(
    private val recentSearchDataSource: RecentSearchDataSource
) : RecentRepository {
    override suspend fun insert(item: String) =
        recentSearchDataSource.insertAndKeepLastTen(item.toRecentSearch())

    override suspend fun getAll(): List<String> =
        recentSearchDataSource.getAll().map { it.toStringQuery() }

    override suspend fun clearAll() = recentSearchDataSource.clearAll()

}