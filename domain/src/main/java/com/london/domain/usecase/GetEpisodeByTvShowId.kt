package com.london.domain.usecase

import com.london.domain.repository.DetailsRepository

class GetEpisodeByTvShowId(
    private val detailsRepository: DetailsRepository
) {
    suspend operator fun invoke(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) =
        detailsRepository.getTvShowEpisodeByPosition(tvShowId, seasonNumber, episodeNumber)
}