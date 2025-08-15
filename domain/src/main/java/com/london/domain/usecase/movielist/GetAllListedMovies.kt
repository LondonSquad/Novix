package com.london.domain.usecase.movielist

import com.london.domain.repository.CustomMovieListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllListedMovies @Inject constructor(
    private val repository: CustomMovieListRepository,
) {

    suspend fun invoke(): Set<Int> = repository.getAllListedMovieIds().toSet()
    fun flow(): Flow<Set<Int>> = repository.getAllListedMovieIdsFlow().map { it.toSet() }
}