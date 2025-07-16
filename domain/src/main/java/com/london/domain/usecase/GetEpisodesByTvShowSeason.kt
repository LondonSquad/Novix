package com.london.domain.usecase

import com.london.domain.repository.DetailsRepository

class GetEpisodesByTvShowSeason(
    private val detailsRepository: DetailsRepository
) {
    suspend operator fun invoke(tvShowId: Int, seasonNumber: Int) =
        detailsRepository.getTvShowEpisodesBySeason(tvShowId, seasonNumber)
}