package com.london.domain.usecase

import com.london.domain.repository.RecentRepository

class AddToRecentSearchUseCase(
    private val recentSearchRepository: RecentRepository<String>
) {
    suspend fun invoke(item: String) = recentSearchRepository.insert(item)
}