package com.london.domain.usecase.movielist

import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class RemoveMovieFromListUseCase @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
) {

    suspend fun invoke(listId: Int, movieId: Int): Boolean =
        customMovieListRepository.removeMovieFromList(listId, movieId)
}