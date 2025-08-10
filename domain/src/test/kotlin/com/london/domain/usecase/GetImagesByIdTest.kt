package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity
import com.london.domain.repository.TvShowRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetImagesByIdTest {
    lateinit var tvShowRepository: TvShowRepository
    lateinit var getTvShowImagesByIdUseCase: GetTvShowImagesByIdUseCase

    @Before
    fun setUp() {
        tvShowRepository = mockk()
        getTvShowImagesByIdUseCase = GetTvShowImagesByIdUseCase(tvShowRepository)
    }

    @Test
    fun `should return backdrops when backdrops are available`() = runTest {
        // Given
        coEvery { tvShowRepository.getImagesTvShowById(TV_SHOW_ID) } returns mockTvShowImagesWithBackdrops
        // When
        val result = getTvShowImagesByIdUseCase.invoke(TV_SHOW_ID)
        // Then
        assertThat(result).isEqualTo(mockTvShowImagesWithBackdrops.backdropsUrl)
    }

    @Test
    fun `should return posters when backdrops are empty but posters are available`() = runTest {
        // Given
        coEvery { tvShowRepository.getImagesTvShowById(TV_SHOW_ID) } returns mockTvShowImagesWithPostersOnly
        // When
        val result = getTvShowImagesByIdUseCase.invoke(TV_SHOW_ID)
        // Then
        assertThat(result).isEqualTo(mockTvShowImagesWithPostersOnly.postersUrl)
    }

    @Test
    fun `should return logos when backdrops and posters are empty but logos are available`() =
        runTest {
            // Given
            coEvery { tvShowRepository.getImagesTvShowById(TV_SHOW_ID) } returns mockTvShowImagesWithLogosOnly
            // When
            val result = getTvShowImagesByIdUseCase.invoke(TV_SHOW_ID)
            // Then
            assertThat(result).isEqualTo(mockTvShowImagesWithLogosOnly.logosUrl)
        }

    @Test
    fun `should return empty list when all image types are empty`() = runTest {
        // Given
        coEvery { tvShowRepository.getImagesTvShowById(TV_SHOW_ID) } returns mockTvShowImagesEmpty
        // When
        val result = getTvShowImagesByIdUseCase.invoke(TV_SHOW_ID)
        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should limit backdrops to 10 items when more than 10 are available`() = runTest {
        // Given
        coEvery { tvShowRepository.getImagesTvShowById(TV_SHOW_ID) } returns mockTvShowImagesWithManyBackdrops
        // When
        val result = getTvShowImagesByIdUseCase.invoke(TV_SHOW_ID)
        // Then
        assertThat(result).hasSize(10)
        assertThat(result).isEqualTo(mockTvShowImagesWithManyBackdrops.backdropsUrl.take(10))
    }

    private companion object {
        const val TV_SHOW_ID = 12345

        val mockTvShowImagesWithBackdrops = TvShowImagesEntity(
            backdropsUrl = listOf("/backdrop1.jpg"),
            id = TV_SHOW_ID,
            logosUrl = listOf("/logo1.jpg"),
            postersUrl = listOf("/poster1.jpg")
        )
        val mockTvShowImagesWithPostersOnly = TvShowImagesEntity(
            backdropsUrl = emptyList(),
            id = TV_SHOW_ID,
            logosUrl = listOf("/logo1.jpg"),
            postersUrl = listOf("/poster1.jpg")
        )

        val mockTvShowImagesWithLogosOnly = TvShowImagesEntity(
            backdropsUrl = emptyList(),
            id = TV_SHOW_ID,
            logosUrl = listOf("/logo1.jpg"),
            postersUrl = emptyList()
        )

        val mockTvShowImagesEmpty = TvShowImagesEntity(
            backdropsUrl = emptyList(),
            id = TV_SHOW_ID,
            logosUrl = emptyList(),
            postersUrl = emptyList()
        )

        val mockTvShowImagesWithManyBackdrops = TvShowImagesEntity(
            backdropsUrl = List(15) { index -> "/backdrop$index.jpg" },
            id = TV_SHOW_ID,
            logosUrl = emptyList(),
            postersUrl = emptyList()
        )
    }
}