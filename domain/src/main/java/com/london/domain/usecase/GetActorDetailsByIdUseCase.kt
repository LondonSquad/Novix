package com.london.domain.usecase

import com.london.domain.repository.ActorRepository

class GetActorDetailsByIdUseCase(
    private val repository: ActorRepository
) {
    suspend fun invoke(actorId: Int) = repository.getActorDetailsById(actorId)
}