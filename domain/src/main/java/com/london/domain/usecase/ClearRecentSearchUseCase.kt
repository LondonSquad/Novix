package com.london.domain.usecase

import com.london.domain.repository.RecentRepository

class ClearRecentSearchUseCase(
    private val recentSearchRepository: RecentRepository
) {
    suspend fun invoke() = recentSearchRepository.clearAll()
}