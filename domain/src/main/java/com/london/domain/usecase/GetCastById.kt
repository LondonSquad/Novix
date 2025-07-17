package com.london.domain.usecase

import com.london.domain.repository.DetailsRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetCastById(
    @Provided
    private val detailsRepository: DetailsRepository
) {
    suspend operator fun invoke(tvShowId: Int) = detailsRepository.getCastTvShowById(tvShowId)
}