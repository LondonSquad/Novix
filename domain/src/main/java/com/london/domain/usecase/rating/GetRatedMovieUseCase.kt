package com.london.domain.usecase.rating

import com.london.domain.repository.myrating.MyRatingRepository

class GetRatedMovieUseCase(
    private val repository: MyRatingRepository
) {
    suspend fun invoke() = repository.getAllRatedMovies()
}