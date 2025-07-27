package com.london.domain.usecase

import com.london.domain.repository.MovieDetailsRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetSimilarMoviesUseCase(
    @Provided
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend fun invoke(movieId: Int) = movieDetailsRepository.getSimilarMoviesById(movieId)
}