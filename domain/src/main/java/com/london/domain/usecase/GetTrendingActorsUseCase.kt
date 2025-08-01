package com.london.domain.usecase

import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.TrendingRepository
import javax.inject.Inject

class GetTrendingActorsUseCase @Inject constructor(
    private val trending: TrendingRepository
) {
    suspend fun invoke(page: Int): PagedFetchResponse<Actor> =
        trending.getTrendingActors(page)
}