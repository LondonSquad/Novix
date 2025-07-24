package com.london.domain.usecase.recent.search

import com.london.domain.entity.recent.RecentSearch
import com.london.domain.repository.RecentRepository
import org.koin.core.annotation.Named
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetRecentSearchUseCase(
    @Provided
    @Named("recentSearchRepository")
    private val recentSearchRepository: RecentRepository<RecentSearch>
) {

    suspend fun invoke(): List<RecentSearch> = recentSearchRepository.getAll()
}
