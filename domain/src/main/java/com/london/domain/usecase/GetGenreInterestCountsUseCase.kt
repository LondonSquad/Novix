package com.london.domain.usecase

import com.london.domain.repository.SearchRepository

class GetGenreInterestCountsUseCase(private val repository: SearchRepository) {
    suspend operator fun invoke(genreType: String) = repository.getGenreInterestCounts(genreType)
}