package com.london.domain.usecase

import com.london.domain.repository.MovieDetailsRepository
import javax.inject.Inject

class GetAccountMovieStatesById @Inject constructor(
    private val repository: MovieDetailsRepository
) {
    suspend operator fun invoke(id: Int): Int =
        repository.getAccountMovieStatesById(
             id = id,
         ).rate
}