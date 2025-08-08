package com.london.domain.usecase

import com.london.domain.repository.discover.DiscoverRepository
import javax.inject.Inject

class GetTvShowsByCategoryId @Inject constructor(
    private val repository: DiscoverRepository
) {
    suspend fun invoke(
        categoryId: Int, pageNumber: Int
    ) = repository.getTvShowsByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )
}
