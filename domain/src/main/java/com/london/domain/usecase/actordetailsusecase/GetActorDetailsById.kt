package com.london.domain.usecase.actordetailsusecase

import com.london.domain.repository.ActorRepository

class GetActorDetailsById(
    private val repository: ActorRepository
) {
    suspend fun invoke(actorId: Int) = repository.getActorDetailsById(actorId)
}