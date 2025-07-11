package com.london.domain.usecase

import com.london.domain.repo.SearchRepository

class GetActorsUseCase(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(name: String, language: String) =
        repository.searchForActors(name, language)
}