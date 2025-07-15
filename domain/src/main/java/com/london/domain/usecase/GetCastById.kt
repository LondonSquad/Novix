package com.london.domain.usecase

import com.london.domain.repository.DetailsRepository

class GetCastById(
    private val detailsRepository: DetailsRepository
) {
    suspend operator fun invoke(tvShowId: Int) = detailsRepository.getCastTvShowById(tvShowId)
}