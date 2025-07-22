package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetUpComingMoviesByCategoryUseCase(
    @Provided
    private val repository: SearchRepository
) {
    suspend operator fun invoke(
        categoryId: Int?, pageNumber: Int
    ) = repository.getUpComingMoviesByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )
}