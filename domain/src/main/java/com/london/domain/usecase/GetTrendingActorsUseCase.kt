package com.london.domain.usecase

import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.TrendingRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetTrendingActorsUseCase(@Provided private val trending: TrendingRepository) {
    suspend fun invoke(page: Int): PagedFetchResponse<Actor> =
        trending.getTrendingActors(page)
}