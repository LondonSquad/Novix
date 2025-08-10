package com.london.domain.usecase

import com.london.domain.repository.ActorRepository
import javax.inject.Inject

class GetActorImagesByIdUseCase @Inject constructor(
    private val repository: ActorRepository
) {
    suspend fun invoke(actorId: Int): List<String> =
        repository.getActorImagesById(actorId).imageUrl
}
