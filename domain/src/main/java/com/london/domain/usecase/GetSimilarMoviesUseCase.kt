package com.london.domain.usecase

import com.london.domain.repository.MovieDetailsRepository

class GetSimilarMoviesUseCase(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend fun invoke(movieId: Int) = movieDetailsRepository.getSimilarMoviesById(movieId)
}