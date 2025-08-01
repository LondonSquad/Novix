package com.london.domain.usecase

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.repository.TrendingRepository
import javax.inject.Inject

class GetTrendingTvShowsUseCase @Inject constructor(
    private val repository: TrendingRepository
) {
    suspend fun invoke(page: Int): PagedFetchResponse<Trending> =
        repository.getTrendingTvShows(page =  page)
}