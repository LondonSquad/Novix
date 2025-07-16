package com.london.domain.usecase

import com.london.domain.repository.MovieDetailsRepository

class GetSimilarMoviesUseCase(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend operator fun invoke(movieId: Int) = movieDetailsRepository.getSimilarMoviesById(movieId)
}