package com.london.domain.usecase.recent.viewed

import com.london.domain.entity.recent.RecentViewed
import com.london.domain.repository.RecentRepository
import javax.inject.Inject

class GetRecentViewedUseCase @Inject constructor(
    private val repository: RecentRepository<RecentViewed>,
) {

    suspend fun invoke() = repository.getAll()
}
