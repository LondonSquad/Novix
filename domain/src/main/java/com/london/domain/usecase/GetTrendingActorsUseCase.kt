package com.london.domain.usecase

import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.ActorRepository
import javax.inject.Inject

class GetTrendingActorsUseCase @Inject constructor(
    private val repository: ActorRepository
) {
    suspend fun invoke(page: Int): PagedFetchResponse<Actor> =
        repository.getTrendingActors(page)
}