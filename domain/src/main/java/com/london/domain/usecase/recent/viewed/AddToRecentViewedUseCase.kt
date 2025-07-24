package com.london.domain.usecase.recent.viewed

import com.london.domain.entity.recent.RecentViewed
import com.london.domain.repository.RecentRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class AddToRecentViewedUseCase(
    @Provided
    @Named("recentViewedRepository")
    private val repository: RecentRepository<RecentViewed>,
) {

    suspend fun invoke(item: RecentViewed) = repository.insert(item)
}
