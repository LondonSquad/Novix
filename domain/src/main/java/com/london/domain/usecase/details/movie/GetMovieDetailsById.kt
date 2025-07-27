package com.london.domain.usecase.details.movie

import com.london.domain.repository.MovieDetailsRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetMovieDetailsById(
    @Provided
    private val movieRepository: MovieDetailsRepository
) {
    suspend fun invoke(movieId: Int) = movieRepository.getMovieById(movieId)
}