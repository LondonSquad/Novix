package com.london.domain.usecase.movielist

import com.london.domain.repository.CustomMovieListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieListsUseCase @Inject constructor(
    private val repository: CustomMovieListRepository
) {
    suspend fun invoke(movieId: Int, forceRefresh: Boolean = false): List<Int> {
        return repository.getMovieListIds(movieId, forceRefresh)
    }

    fun flow(movieId: Int): Flow<List<Int>> {
        return repository.getMovieListIdsFlow(movieId)
    }
}