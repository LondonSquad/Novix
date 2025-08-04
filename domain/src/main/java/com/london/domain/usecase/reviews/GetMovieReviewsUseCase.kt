package com.london.domain.usecase.reviews

import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetMovieReviewsUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend fun invoke(movieId: Int, pageNumber: Int) =
        tvShowRepository.getMovieReviews(movieId, pageNumber)
}