package com.london.domain.usecase

import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetEpisodeVideoProviderUseCase @Inject constructor(
    private val repository: TvShowRepository
) {
    suspend fun invoke(seriesId: Int, seasonNumber: Int, episodeNumber: Int)
    = repository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
}