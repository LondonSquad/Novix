package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetMoviesUseCase(
    @Provided
    private val repository: SearchRepository
) {
    suspend operator fun invoke(
        name: String,
        language: String,
        pageNumber: Int
    ) = repository.searchForMovies(
        name = name,
        language = language,
        pageNumber = pageNumber
    )
}