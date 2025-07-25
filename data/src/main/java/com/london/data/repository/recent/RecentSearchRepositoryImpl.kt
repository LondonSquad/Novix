package com.london.data.repository.recent

import com.london.data.datasource.local.model.recent.RecentSearchLocal
import com.london.data.datasource.local.recent.RecentDataSource
import com.london.data.mapper.recent.toEntity
import com.london.data.mapper.recent.toRecentSearch
import com.london.domain.entity.recent.RecentSearch
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
) : RecentRepository<RecentSearch> {
    override suspend fun insert(item: RecentSearch) =
        recentSearchLocalDataSource.insertAndKeepLastTen(item.toRecentSearch())

    override suspend fun getAll(): List<RecentSearch> =
        recentSearchLocalDataSource.getAll().map { it.toEntity() }

    override suspend fun clearAll() = recentSearchLocalDataSource.clearAll()

    override suspend fun delete(item: RecentSearch) {
        recentSearchLocalDataSource.delete(item.toRecentSearch())
    }
}
