package com.london.domain.usecase

import com.london.domain.repository.ActorRepository

class GetActorImagesByIdUseCase(
    private val repository: ActorRepository
) {
    suspend fun invoke(actorId: Int) = repository.getActorImagesById(actorId)
}