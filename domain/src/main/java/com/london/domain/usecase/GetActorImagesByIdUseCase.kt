package com.london.domain.usecase

import com.london.domain.entity.actordetails.actorimage.ImageDetails
import com.london.domain.repository.ActorRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetActorImagesByIdUseCase(
    @Provided
    private val repository: ActorRepository
) {
    suspend fun invoke(actorId: Int) : List<ImageDetails>
    {
        val images = repository.getActorImagesById(actorId)

        return images.profiles

    }
}