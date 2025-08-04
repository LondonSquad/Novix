package com.london.domain.usecase

import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetEpisodeByTvShowId @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) =
        tvShowRepository.getTvShowEpisodeByPosition(tvShowId, seasonNumber, episodeNumber)
}