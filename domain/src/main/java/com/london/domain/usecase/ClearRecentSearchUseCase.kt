package com.london.domain.usecase

import com.london.domain.repository.RecentRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class ClearRecentSearchUseCase(
    @Provided
    @Named("recentSearchRepository")
    private val recentSearchRepository: RecentRepository<String>
) {
    suspend fun invoke() = recentSearchRepository.clearAll()
}