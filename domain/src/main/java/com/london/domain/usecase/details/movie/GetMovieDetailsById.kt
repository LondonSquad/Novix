package com.london.domain.usecase.details.movie

import com.london.domain.repository.MovieDetailsRepository
import javax.inject.Inject

class GetMovieDetailsById @Inject constructor(
    private val movieRepository: MovieDetailsRepository
) {
    suspend fun invoke(movieId: Int) = movieRepository.getMovieById(movieId)
}