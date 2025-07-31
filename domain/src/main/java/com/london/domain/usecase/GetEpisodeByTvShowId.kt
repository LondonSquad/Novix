package com.london.domain.usecase

import com.london.domain.repository.DetailsRepository
import javax.inject.Inject

class GetEpisodeByTvShowId @Inject constructor(
    private val detailsRepository: DetailsRepository
) {
    suspend operator fun invoke(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) =
        detailsRepository.getTvShowEpisodeByPosition(tvShowId, seasonNumber, episodeNumber)
}