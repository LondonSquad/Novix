package com.london.domain.usecase.recent.viewed

import com.london.domain.entity.recent.RecentViewed
import com.london.domain.repository.RecentRepository
import javax.inject.Inject

class ManageRecentViewedUseCase @Inject constructor(
    private val repository: RecentRepository<RecentViewed>,
) {

    suspend fun addToRecentViewed(item: RecentViewed) = repository.insert(item)

    suspend fun getRecentViewed() = repository.getAll()

    suspend fun clearRecentViewed() = repository.clearAll()
}
