package com.london.domain.usecase.movielist

import com.london.domain.repository.CustomMovieListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsMovieListedUseCase @Inject constructor(
    private val repository: CustomMovieListRepository
) {
    suspend fun invoke(
        movieId: Int,
        forceRefresh: Boolean = false
    ): Boolean = repository.isMovieListed(movieId = movieId, forceRefresh = forceRefresh)

    fun asFlow(movieId: Int): Flow<Boolean> = repository.isMovieListedFlow(movieId = movieId)
}
