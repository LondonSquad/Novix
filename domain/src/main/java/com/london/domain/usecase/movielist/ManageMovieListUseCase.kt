package com.london.domain.usecase.movielist

import com.london.domain.repository.CustomMovieListRepository
import javax.inject.Inject

class ManageMovieListUseCase @Inject constructor(
    private val customMovieListRepository: CustomMovieListRepository,
) {

    suspend fun createMovieList(name: String): Boolean = customMovieListRepository.createMovieList(name)
    suspend fun deleteMovieList(id: UInt): Boolean = customMovieListRepository.deleteMovieList(id)
}
