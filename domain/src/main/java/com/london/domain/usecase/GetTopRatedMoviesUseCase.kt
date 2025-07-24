package com.london.domain.usecase

import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.repository.TopRatedMovieRepo
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetTopRatedMoviesUseCase(
    @Provided
    private val topRatedMovieRepo: TopRatedMovieRepo
) {
    suspend operator fun invoke(
        pageNumber: Int,
        language: String,
        region: String
    ): List<TopRatedMovie> = topRatedMovieRepo.getTopRatedMovies(
        pageNumber,
        language,
        region
    )
}