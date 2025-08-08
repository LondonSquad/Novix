package com.london.domain.usecase.rating

import com.london.domain.repository.myrating.MyRatingRepository
import javax.inject.Inject

class GetRatedMovieUseCase @Inject constructor(
    private val repository: MyRatingRepository
) {
    suspend fun invoke() = repository.getAllRatedMovies()
}