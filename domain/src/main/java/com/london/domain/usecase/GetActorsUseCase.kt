package com.london.domain.usecase

import com.london.domain.repository.SearchRepository


class GetActorsUseCase(
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
