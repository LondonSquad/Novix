package com.london.domain.usecase.toppicks

import com.london.domain.repository.MovieRepository
import javax.inject.Inject

class GetActorMoviePicksByIdUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend fun invoke(actorId: Int) = repository.getActorMoviePicksById(actorId)
}