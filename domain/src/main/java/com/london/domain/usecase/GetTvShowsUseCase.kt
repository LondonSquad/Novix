package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetTvShowsUseCase(
    @Provided
    private val repository: SearchRepository
) {
    suspend operator fun invoke(
        name: String,
        pageNumber: Int
    ) = repository.searchForTvShows(
        name = name,
        pageNumber = pageNumber
    )
}