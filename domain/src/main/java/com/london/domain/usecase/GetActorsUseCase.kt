package com.london.domain.usecase

import com.london.domain.repository.SearchRepository
import javax.inject.Inject

class GetActorsUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(
        name: String,
        pageNumber: Int
    ) = repository.searchForActors(
        name = name,
        pageNumber = pageNumber
    )
}