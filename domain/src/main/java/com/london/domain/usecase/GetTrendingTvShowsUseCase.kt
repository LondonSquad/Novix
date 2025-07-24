package com.london.domain.usecase

import com.london.domain.entity.trending.TrendingTvShow
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.TrendingTvShowRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetTrendingTvShowsUseCase(
    @Provided private val trendingTvShowRepository: TrendingTvShowRepository
) {
    suspend operator fun invoke(page: Int): PagedFetchResponse<TrendingTvShow> = trendingTvShowRepository.getTrendingTvShows(page)
} 