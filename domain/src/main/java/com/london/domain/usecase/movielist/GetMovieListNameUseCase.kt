package com.london.domain.usecase.movielist

import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class GetMovieListNameUseCase @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
) {

    suspend fun invoke(listId: UInt): String =
        customMovieListRepository.getMovieListName(listId)
}