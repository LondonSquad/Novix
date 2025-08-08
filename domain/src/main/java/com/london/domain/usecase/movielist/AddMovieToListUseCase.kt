package com.london.domain.usecase.movielist

import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class AddMovieToListUseCase @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
) {

    suspend fun invoke(listId: UInt, movieId: UInt): Boolean =
        customMovieListRepository.addMovieToList(listId, movieId)
}
