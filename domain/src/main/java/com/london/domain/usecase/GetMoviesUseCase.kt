package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import org.koin.core.annotation.Single
import javax.inject.Inject

@Single
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