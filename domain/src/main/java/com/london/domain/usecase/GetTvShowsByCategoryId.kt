package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import javax.inject.Inject

class GetTvShowsByCategoryId @Inject constructor(
    private val repository: SearchRepository
) {
    suspend fun invoke(
        categoryId: Int, pageNumber: Int
    ) = repository.searchForTvShowByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )
}