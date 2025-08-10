package com.london.domain.usecase

import com.london.domain.KoverIgnore
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

@KoverIgnore
class GetTvShowImagesByIdUseCase @Inject constructor(
    private val repository: TvShowRepository
) {
    suspend operator fun invoke(tvShowId: Int): List<String> {
        val images = repository.getImagesTvShowById(tvShowId)
        return when {
            images.backdropsUrl.isNotEmpty() -> images.backdropsUrl
            images.postersUrl.isNotEmpty() -> images.postersUrl
            images.logosUrl.isNotEmpty() -> images.logosUrl
            else -> emptyList()
        }.take(10)
    }
}