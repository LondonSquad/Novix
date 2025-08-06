package com.london.domain.usecase

import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetEpisodesByTvShowSeason @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvShowId: Int, seasonNumber: Int) =
        tvShowRepository.getTvShowEpisodesBySeason(tvShowId, seasonNumber)
}