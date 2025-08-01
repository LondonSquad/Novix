package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import javax.inject.Inject

class GetGenreInterestCountsUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend fun invoke(mediaType: String) = repository.getGenreInterestCounts(mediaType)
}
