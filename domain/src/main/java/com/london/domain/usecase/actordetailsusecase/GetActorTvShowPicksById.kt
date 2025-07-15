package com.london.domain.usecase.actordetailsusecase

import com.london.domain.repository.DetailsRepository

class GetActorTvShowPicksById(
    private val repository: DetailsRepository
) {
    suspend fun invoke(actorId: Int) = repository.getActorTvShowPicksById(actorId)
}