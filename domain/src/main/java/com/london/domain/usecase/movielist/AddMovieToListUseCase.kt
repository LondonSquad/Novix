package com.london.domain.usecase.movielist

import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class AddMovieToListUseCase @Inject constructor(
    private val repository: CustomMovieListRepository,
) {

    suspend fun invoke(listId: Int, movieId: Int): Boolean =
        repository.addMovieToList(listId, movieId)
}
