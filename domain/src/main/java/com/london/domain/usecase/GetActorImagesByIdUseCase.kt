package com.london.domain.usecase

import com.london.domain.entity.actordetails.actorimage.ImageDetails
import com.london.domain.repository.ActorRepository
import javax.inject.Inject

class GetActorImagesByIdUseCase @Inject constructor(
    private val repository: ActorRepository
) {
    suspend fun invoke(actorId: Int) : List<ImageDetails>
    {
        val images = repository.getActorImagesById(actorId)

        return images.profiles

    }
}