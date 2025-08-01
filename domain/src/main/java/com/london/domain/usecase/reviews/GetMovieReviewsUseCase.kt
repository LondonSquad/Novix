package com.london.domain.usecase.reviews

import com.london.domain.repository.DetailsRepository
import javax.inject.Inject

class GetMovieReviewsUseCase @Inject constructor(
    private val detailsRepository: DetailsRepository
) {
    suspend fun invoke(movieId: Int, pageNumber: Int) =
        detailsRepository.getMovieReviews(movieId, pageNumber)
}