package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetGenreInterestCountsUseCase(
    @Provided
    private val repository: SearchRepository
) {
    suspend fun invoke(mediaType: String) = repository.getGenreInterestCounts(mediaType)
}
