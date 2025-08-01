package com.london.domain.usecase

import com.london.domain.repository.TvShowVideoProviderRepository
import javax.inject.Inject

class GetTvShowVideoProvider @Inject constructor(
    private val repository: TvShowVideoProviderRepository
) {
    suspend fun invoke(tvShowId: Int) = repository.getTvShowVideos(tvShowId)
}