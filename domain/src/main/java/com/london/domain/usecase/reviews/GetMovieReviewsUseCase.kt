package com.london.domain.usecase.reviews

import com.london.domain.repository.MovieDetailsRepository
import javax.inject.Inject

class GetMovieReviewsUseCase @Inject constructor(
    private val movieRepository: MovieDetailsRepository
) {
    suspend fun invoke(movieId: Int, pageNumber: Int) =
        movieRepository.getMovieReviews(movieId, pageNumber)
}