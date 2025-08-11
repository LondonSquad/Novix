package com.london.domain.usecase

import com.london.domain.repository.MovieRepository
import javax.inject.Inject

class GetMoviesByCategoryUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(
        categoryId: Int, pageNumber: Int
    ) = repository.getMoviesByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )
}
