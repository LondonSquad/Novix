package com.london.domain.usecase.movielist

import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class ManageMovieListUseCase @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
) {
    suspend fun createMovieList(name: String): Boolean =
        customMovieListRepository.createMovieList(name)

    suspend fun deleteMovieList(id: Int): Boolean =
        customMovieListRepository.deleteMovieList(id)

    suspend fun addMovieToList(listId: Int, movieId: Int): Boolean =
        customMovieListRepository.addMovieToList(listId, movieId)

    suspend fun removeMovieFromList(listId: Int, movieId: Int): Boolean =
        customMovieListRepository.removeMovieFromList(listId, movieId)
}
