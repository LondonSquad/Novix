package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(
        name: String,
        pageNumber: Int
    ) = repository.searchForMovies(
        name = name,
        pageNumber = pageNumber
    )
}