package com.london.domain.usecase

import com.london.domain.repository.MovieRepository
import javax.inject.Inject

class GetUpComingMoviesByCategoryUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(
        categoryId: Int?, pageNumber: Int
    ) = repository.getUpcomingMoviesByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )
}