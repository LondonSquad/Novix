package com.london.domain.usecase.actordetailsusecase

import com.london.domain.repository.ActorRepository

class GetActorTvShowPicksByIdUseCase(
    private val repository: ActorRepository
) {
    suspend fun invoke(actorId: Int) = repository.getActorTvShowPicksById(actorId)
}