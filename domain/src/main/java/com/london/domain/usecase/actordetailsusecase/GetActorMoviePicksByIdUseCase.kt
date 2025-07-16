package com.london.domain.usecase.actordetailsusecase

import com.london.domain.repository.ActorRepository

class GetActorMoviePicksByIdUseCase(
    private val repository: ActorRepository
) {
    suspend fun invoke(actorId: Int) = repository.getActorMoviePicksById(actorId)
}