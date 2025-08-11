package com.london.domain.usecase.toppicks

import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetActorTvShowPicksByIdUseCase @Inject constructor(
    private val repository: TvShowRepository
) {
    suspend fun invoke(actorId: Int) = repository.getActorTvShowPicksById(actorId)
}