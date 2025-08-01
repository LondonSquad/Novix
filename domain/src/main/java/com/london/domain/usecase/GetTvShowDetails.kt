package com.london.domain.usecase

import com.london.domain.repository.DetailsRepository
import javax.inject.Inject

class GetTvShowDetails @Inject constructor(
    private val detailsRepository: DetailsRepository
) {
    suspend operator fun invoke(tvShowId: Int) =
        detailsRepository.getTvShowDetailsById(tvShowId)
}