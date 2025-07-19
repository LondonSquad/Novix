package com.london.domain.usecase

import com.london.domain.repository.DetailsRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetEpisodeByTvShowId(
    @Provided
    private val detailsRepository: DetailsRepository
) {
    suspend operator fun invoke(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) =
        detailsRepository.getTvShowEpisodeByPosition(tvShowId, seasonNumber, episodeNumber)
}