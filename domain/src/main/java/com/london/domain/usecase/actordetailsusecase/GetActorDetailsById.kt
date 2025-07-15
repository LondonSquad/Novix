package com.london.domain.usecase.actordetailsusecase

import com.london.domain.repository.DetailsRepository

class GetActorDetailsById(
    private val repository: DetailsRepository
) {
    suspend fun invoke(actorId: Int) = repository.getActorDetailsById(actorId)
}