package com.london.domain.usecase

import com.london.domain.repository.MovieDetailsRepository

class GetMovieImagesUseCase (
    private val movieDetailsRepository: MovieDetailsRepository
){
    suspend fun invoke(movieId: Int) = movieDetailsRepository.getMovieImagesById(movieId)
}