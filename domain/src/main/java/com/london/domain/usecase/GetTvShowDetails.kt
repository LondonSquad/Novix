package com.london.domain.usecase

import com.london.domain.repository.DetailsRepository

class GetTvShowDetails(
    private val detailsRepository: DetailsRepository
) {
    suspend operator fun invoke(tvShowId: Int) =
        detailsRepository.getTvShowDetailsById(tvShowId)
}