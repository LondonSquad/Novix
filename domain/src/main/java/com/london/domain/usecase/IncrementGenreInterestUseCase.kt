package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class IncrementGenreInterestUseCase(
    @Provided
    private val repository: SearchRepository
) {
    suspend fun invoke(genreId: Int, mediaType: String) =
        repository.incrementGenreInterest(genreId, mediaType)
}
