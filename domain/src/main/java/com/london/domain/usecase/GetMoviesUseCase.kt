package com.london.domain.usecase

import com.london.domain.repo.SearchRepository

class GetMoviesUseCase(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(name: String, language: String) =
        repository.searchForMovies(name, language)
}