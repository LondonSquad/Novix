package com.london.domain.usecase.recent.search

import com.london.domain.entity.recent.RecentSearch
import com.london.domain.repository.RecentRepository
import javax.inject.Inject

class AddToRecentSearchUseCase @Inject constructor(
    private val recentSearchRepository: RecentRepository<RecentSearch>
) {

    suspend fun invoke(item: RecentSearch) = recentSearchRepository.insert(item)
}
