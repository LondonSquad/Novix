package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import javax.inject.Inject

class GetMoviesByCategoryUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(
        categoryId: Int, pageNumber: Int
    ) = repository.searchForMoviesByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )
}