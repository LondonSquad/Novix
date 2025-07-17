package com.london.data.repository.recent

import com.london.data.datasource.local.model.recent.RecentSearchLocal
import com.london.data.datasource.local.recent.RecentDataSource
import com.london.data.mapper.recent.toRecentSearch
import com.london.data.mapper.recent.toStringQuery
import com.london.domain.repository.RecentRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
@Named("recentSearchRepository")
class RecentSearchRepositoryImpl(
    @Provided
    @Named("recentSearchDataSource")
    private val recentSearchLocalDataSource: RecentDataSource<RecentSearchLocal>
) : RecentRepository<String> {
    override suspend fun insert(item: String) =
        recentSearchLocalDataSource.insertAndKeepLastTen(item.toRecentSearch())

    override suspend fun getAll(): List<String> =
        recentSearchLocalDataSource.getAll().map { it.toStringQuery() }

    override suspend fun clearAll() = recentSearchLocalDataSource.clearAll()

}