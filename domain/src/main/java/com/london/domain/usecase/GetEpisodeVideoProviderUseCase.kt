package com.london.domain.usecase

import com.london.domain.repository.DetailsRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetEpisodeVideoProviderUseCase(
    @Provided
    private val repository: DetailsRepository
) {
    suspend fun invoke(seriesId: Int, seasonNumber: Int, episodeNumber: Int)
    = repository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
}