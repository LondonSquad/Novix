package com.london.domain.usecase

import com.london.domain.repository.discover.DiscoverRepository
import javax.inject.Inject

class GetMoviesByCategoryUseCase @Inject constructor(
    private val repository: DiscoverRepository
) {
    suspend operator fun invoke(
        categoryId: Int, pageNumber: Int
    ) = repository.getMoviesByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )
}
