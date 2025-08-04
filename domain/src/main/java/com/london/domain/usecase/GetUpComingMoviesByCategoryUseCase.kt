package com.london.domain.usecase

import com.london.domain.repository.UpComingRepository
import javax.inject.Inject

class GetUpComingMoviesByCategoryUseCase @Inject constructor(
    private val repository: UpComingRepository
) {
    suspend operator fun invoke(
        categoryId: Int?, pageNumber: Int
    ) = repository.getUpComingMoviesByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )
}