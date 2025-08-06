package com.london.domain.usecase.toppicks

import com.london.domain.repository.ActorRepository
import javax.inject.Inject

class GetActorMoviePicksByIdUseCase @Inject constructor(
    private val repository: ActorRepository
) {
    suspend fun invoke(actorId: Int) = repository.getActorMoviePicksById(actorId)
}