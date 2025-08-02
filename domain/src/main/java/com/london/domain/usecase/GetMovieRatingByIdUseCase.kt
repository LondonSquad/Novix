package com.london.domain.usecase

import com.london.domain.repository.MovieDetailsRepository
import javax.inject.Inject

class GetMovieRatingByIdUseCase @Inject constructor(
    private val repository: MovieDetailsRepository
) {
    suspend operator fun invoke(id: Int): Int =
        repository.getMovieAccountStatesById(
             id = id,
         ).rate
}