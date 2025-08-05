package com.london.domain.usecase.movielist

import com.london.domain.repository.MovieListRepository
import javax.inject.Inject

class ManageMovieListUseCase @Inject constructor(
    private val movieListRepository: MovieListRepository,
) {

    suspend fun createMovieList(name: String): Boolean = movieListRepository.createMovieList(name)
    suspend fun deleteMovieList(id: UInt): Boolean = movieListRepository.deleteMovieList(id)
}
