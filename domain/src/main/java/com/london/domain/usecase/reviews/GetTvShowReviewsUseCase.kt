package com.london.domain.usecase.reviews

import com.london.domain.repository.DetailsRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetTvShowReviewsUseCase(
    @Provided
    private val detailsRepository: DetailsRepository
) {
    suspend fun invoke(movieId: Int, pageNumber: Int) =
        detailsRepository.getTvShowReviews(movieId, pageNumber)
}