package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetActorsUseCase(
    @Provided
    private val repository: SearchRepository
) {
    suspend operator fun invoke(
        name: String,
        language: String,
        pageNumber: Int
    ) = repository.searchForActors(
        name = name,
        language = language,
        pageNumber = pageNumber
    )
}