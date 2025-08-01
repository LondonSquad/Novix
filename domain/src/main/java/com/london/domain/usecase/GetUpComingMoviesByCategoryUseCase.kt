package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import javax.inject.Inject

class GetUpComingMoviesByCategoryUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(
        categoryId: Int?, pageNumber: Int
    ) = repository.getUpComingMoviesByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )
}