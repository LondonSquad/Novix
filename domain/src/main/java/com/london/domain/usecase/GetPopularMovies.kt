package com.london.domain.usecase

import com.london.domain.repository.MovieRepository
import javax.inject.Inject

class GetPopularMovies @Inject constructor(
    private val popularRepository: MovieRepository
) {
    suspend fun invoke(limit: Int = LIMIT) = popularRepository.getPopularMovies().take(limit)

    companion object{
        const val LIMIT = 5
    }
}