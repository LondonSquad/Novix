package com.london.domain.usecase

import com.london.domain.repository.TopRatedMovieRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetTopRatedMoviesUseCase(
    @Provided
    private val topRatedMovieRepo: TopRatedMovieRepository
) {
    suspend operator fun invoke(
        pageNumber: Int,
    ) = topRatedMovieRepo.getTopRatedMovies(
        pageNumber,
    )
}