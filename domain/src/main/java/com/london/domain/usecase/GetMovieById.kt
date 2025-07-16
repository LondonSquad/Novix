package com.london.domain.usecase

import com.london.domain.repository.MovieDetailsRepository

class GetMovieById(
    private val movieRepository: MovieDetailsRepository
) {
    suspend fun invoke(movieId: Int) = movieRepository.getMovieById(movieId)
}