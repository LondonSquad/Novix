package com.london.domain.usecase

import com.london.domain.repository.ActorRepository
import javax.inject.Inject


class GetActorDetailsByIdUseCase @Inject constructor(
    private val repository: ActorRepository
) {
    suspend fun invoke(actorId: Int) = repository.getActorDetailsById(actorId)
}