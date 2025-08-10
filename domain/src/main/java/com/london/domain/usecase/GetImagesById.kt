package com.london.domain.usecase

import com.london.domain.KoverIgnore
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

@KoverIgnore
class GetImagesById @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend operator fun invoke(tvShowId: Int): List<String> {
        val images = tvShowRepository.getImagesTvShowById(tvShowId)

        return when {
            images.backdropsUrl.isNotEmpty() -> images.backdropsUrl.take(10)
            images.postersUrl.isNotEmpty() -> images.postersUrl.take(10)
            images.logosUrl.isNotEmpty() -> images.logosUrl.take(10)
            else -> emptyList()
        }
    }
}