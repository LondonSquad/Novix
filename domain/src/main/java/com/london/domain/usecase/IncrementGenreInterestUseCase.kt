package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import javax.inject.Inject

class IncrementGenreInterestUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend fun invoke(genreId: Int, mediaType: String) =
        repository.incrementGenreInterest(genreId, mediaType)
}
