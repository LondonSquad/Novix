package com.london.domain.usecase

import com.london.domain.repository.ActorRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetActorTvShowPicksByIdUseCase(
    @Provided
    private val repository: ActorRepository
) {
    suspend fun invoke(actorId: Int) = repository.getActorTvShowPicksById(actorId)
}