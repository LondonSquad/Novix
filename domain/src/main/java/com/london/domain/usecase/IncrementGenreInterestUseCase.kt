package com.london.domain.usecase

import com.london.domain.repository.SearchRepository

class IncrementGenreInterestUseCase(private val repository: SearchRepository) {
    suspend operator fun invoke(genreId: Int, genreType: String) =
        repository.incrementGenreInterest(genreId, genreType)
}