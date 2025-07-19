package com.london.domain.usecase

import com.london.domain.repository.TvShowVideoProviderRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetTvShowVideoProvider(
    @Provided
    private val repository: TvShowVideoProviderRepository
) {
    suspend fun invoke(tvShowId: Int) = repository.getTvShowVideos(tvShowId)
}