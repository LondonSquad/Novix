package com.london.domain.usecase.recent.search

import com.london.domain.entity.recent.RecentSearch
import com.london.domain.repository.RecentRepository
import javax.inject.Inject

class ManageRecentSearchUseCase @Inject constructor(
    private val recentSearchRepository: RecentRepository<RecentSearch>
) {
    suspend fun addToRecentSearch(item: RecentSearch) = recentSearchRepository.insert(item)

    suspend fun deleteRecentSearch(item: RecentSearch) = recentSearchRepository.delete(item)

    suspend fun getRecentSearch() = recentSearchRepository.getAll()

    suspend fun clearRecentSearch() = recentSearchRepository.clearAll()
}
