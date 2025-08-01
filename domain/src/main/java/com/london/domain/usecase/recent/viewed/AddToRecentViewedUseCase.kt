package com.london.domain.usecase.recent.viewed

import com.london.domain.entity.recent.RecentViewed
import com.london.domain.repository.RecentRepository
import javax.inject.Inject

class AddToRecentViewedUseCase @Inject constructor(
    private val repository: RecentRepository<RecentViewed>,
) {

    suspend fun invoke(item: RecentViewed) = repository.insert(item)
}
