package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import javax.inject.Inject

class GetTvShowsUseCase @Inject constructor(
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