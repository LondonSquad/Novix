package com.london.domain.usecase

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.repository.TrendingRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetTrendingMoviesUseCase(@Provided private val repository: TrendingRepository) {
    suspend fun invoke(page: Int): PagedFetchResponse<Trending> =
        repository.getTrendingMovies(page)
}