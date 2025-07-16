package com.london.domain.usecase

import com.london.domain.repository.SearchRepository

class GetGenreInterestCountsUseCase(private val repository: SearchRepository) {
    suspend fun invoke(mediaType: String) = repository.getGenreInterestCounts(mediaType)
}
