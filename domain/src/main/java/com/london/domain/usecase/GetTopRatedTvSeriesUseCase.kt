package com.london.domain.usecase

import com.london.domain.entity.toprated.TopRatedTvSeries
import com.london.domain.repository.TopRatedTvSeriesRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetTopRatedTvSeriesUseCase(
    @Provided
    private val topRatedTvSeriesRepo: TopRatedTvSeriesRepository
) {
    suspend operator fun invoke(
        pageNumber: Int,
        language: String
    ): List<TopRatedTvSeries> = topRatedTvSeriesRepo.getTopRatedTvSeries(
        pageNumber,
        language,
    )
}