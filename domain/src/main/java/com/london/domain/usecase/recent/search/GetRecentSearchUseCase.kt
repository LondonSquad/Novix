package com.london.domain.usecase.recent.search

import com.london.domain.entity.recent.RecentSearch
import com.london.domain.repository.RecentRepository
import javax.inject.Inject

class GetRecentSearchUseCase @Inject constructor(
    private val recentSearchRepository: RecentRepository<RecentSearch>
) {

    suspend fun invoke(): List<RecentSearch> = recentSearchRepository.getAll()
}
