package com.london.domain.usecase

import com.london.domain.repository.SearchRepository

class IncrementGenreInterestUseCase(private val repository: SearchRepository) {
    suspend fun invoke(genreId: Int, mediaType: String) =
        repository.incrementGenreInterest(genreId, mediaType)
}
