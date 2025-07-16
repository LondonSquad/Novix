package com.london.domain.usecase

import com.london.domain.repository.RecentRepository

class GetRecentSearchUseCase(
    private val recentSearchRepository: RecentRepository<String>

) {
    suspend fun invoke(): List<String> = recentSearchRepository.getAll()
}