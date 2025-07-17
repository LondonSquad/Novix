package com.london.data.repository.recent

import com.london.data.datasource.local.model.recent.RecentViewedLocal
import com.london.data.datasource.local.recent.RecentDataSource
import com.london.data.mapper.recent.toEntity
import com.london.data.mapper.recent.toLocal
import com.london.domain.entity.recent.RecentViewed
import com.london.domain.repository.RecentRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
@Named("recentViewedRepository")
data class RecentViewedRepositoryImpl(
    @Provided
    @Named("recentViewedDataSource")
    private val recentRecentViewedLocalDataSource: RecentDataSource<RecentViewedLocal>
) : RecentRepository<RecentViewed> {
    override suspend fun insert(item: RecentViewed) =
        recentRecentViewedLocalDataSource.insertAndKeepLastTen(item.toLocal())

    override suspend fun getAll(): List<RecentViewed> =
        recentRecentViewedLocalDataSource.getAll().map { it.toEntity() }

    override suspend fun clearAll() = recentRecentViewedLocalDataSource.clearAll()

}