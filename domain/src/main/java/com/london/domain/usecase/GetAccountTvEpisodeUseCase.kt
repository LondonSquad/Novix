package com.london.domain.usecase

import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetAccountTvEpisodeUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend fun invoke(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int

    ) = tvShowRepository.getAccountTvEpisode(
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber
    )
}