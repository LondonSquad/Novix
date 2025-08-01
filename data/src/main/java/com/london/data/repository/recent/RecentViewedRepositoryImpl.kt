package com.london.data.repository.recent

import com.london.data.local.model.recent.viewed.RecentViewedLocal
import com.london.data.local.source.recent.RecentDataSource
import com.london.data.mapper.recent.toEntity
import com.london.data.mapper.recent.toLocal
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.repository.RecentRepository
import javax.inject.Inject

data class RecentViewedRepositoryImpl @Inject constructor(
    private val recentRecentViewedLocalDataSource: RecentDataSource<RecentViewedLocal>
) : RecentRepository<RecentViewed> {
    override suspend fun insert(item: RecentViewed) =
        recentRecentViewedLocalDataSource.insertAndKeepLastTen(item.toLocal())

    override suspend fun getAll(): List<RecentViewed> =
        recentRecentViewedLocalDataSource.getAll().map { it.toEntity() }

    override suspend fun clearAll() = recentRecentViewedLocalDataSource.clearAll()

    override suspend fun delete(item: RecentViewed) {
        recentRecentViewedLocalDataSource.delete(item.toLocal())
    }
}