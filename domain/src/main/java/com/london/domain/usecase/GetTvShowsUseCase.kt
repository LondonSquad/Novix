package com.london.domain.usecase

import com.london.domain.repository.SearchRepository

class GetTvShowsUseCase(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(name: String, language: String) =
        repository.searchForTvShows(name, language)
}