package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetTvShowsByCategoryId(
    @Provided
    private val repository: SearchRepository
) {
    suspend fun invoke(
        categoryId: Int, pageNumber: Int
    ) = repository.searchForTvShowByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )
}