package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.GetImagesByIdFailedException
import com.london.domain.entity.tvshowdetails.ImageItemEntity
import com.london.domain.entity.tvshowdetails.TvShowImagesEntity
import com.london.domain.repository.DetailsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetImagesByIdTest {
    lateinit var detailsRepository: DetailsRepository
    lateinit var getImagesById: GetImagesById

    @Before
    fun setUp() {
        detailsRepository = mockk()
        getImagesById = GetImagesById(detailsRepository)
    }

    @Test
    fun `should return backdrops when backdrops are available`() = runTest {
        // Given
        coEvery { detailsRepository.getImagesTvShowById(TV_SHOW_ID) } returns mockTvShowImagesWithBackdrops
        // When
        val result = getImagesById(TV_SHOW_ID)
        // Then
        assertThat(result).isEqualTo(mockTvShowImagesWithBackdrops.backdrops)
    }

    @Test
    fun `should return posters when backdrops are empty but posters are available`() = runTest {
        // Given
        coEvery { detailsRepository.getImagesTvShowById(TV_SHOW_ID) } returns mockTvShowImagesWithPostersOnly
        // When
        val result = getImagesById(TV_SHOW_ID)
        // Then
        assertThat(result).isEqualTo(mockTvShowImagesWithPostersOnly.posters)
    }

    @Test
    fun `should return logos when backdrops and posters are empty but logos are available`() = runTest {
        // Given
        coEvery { detailsRepository.getImagesTvShowById(TV_SHOW_ID) } returns mockTvShowImagesWithLogosOnly
        // When
        val result = getImagesById(TV_SHOW_ID)
        // Then
        assertThat(result).isEqualTo(mockTvShowImagesWithLogosOnly.logos)
    }

    @Test
    fun `should return empty list when all image types are empty`() = runTest {
        // Given
        coEvery { detailsRepository.getImagesTvShowById(TV_SHOW_ID) } returns mockTvShowImagesEmpty
        // When
        val result = getImagesById(TV_SHOW_ID)
        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should limit backdrops to 10 items when more than 10 are available`() = runTest {
        // Given
        coEvery { detailsRepository.getImagesTvShowById(TV_SHOW_ID) } returns mockTvShowImagesWithManyBackdrops
        // When
        val result = getImagesById(TV_SHOW_ID)
        // Then
        assertThat(result).hasSize(10)
        assertThat(result).isEqualTo(mockTvShowImagesWithManyBackdrops.backdrops.take(10))
    }

    @Test
    fun `should throw exception when repository throws exception`() = runTest {
        // Given
        coEvery { detailsRepository.getImagesTvShowById(TV_SHOW_ID) } throws GetImagesByIdFailedException()
        // When & Then
        assertThrows<GetImagesByIdFailedException> {
            getImagesById(TV_SHOW_ID)
        }
    }

    private companion object {
        const val TV_SHOW_ID = 12345

        val mockImageItem = ImageItemEntity(
            aspectRatio = 1.78,
            height = 1080,
            iso6391 = "en",
            fileUrl = "/backdrop1.jpg",
            voteAverage = 8.5,
            voteCount = 100,
            width = 1920
        )

        val mockTvShowImagesWithBackdrops = TvShowImagesEntity(
            backdrops = listOf(mockImageItem),
            id = TV_SHOW_ID,
            logos = listOf(
                ImageItemEntity(
                    aspectRatio = 1.0,
                    height = 500,
                    iso6391 = null,
                    fileUrl = "/logo1.jpg",
                    voteAverage = 7.8,
                    voteCount = 50,
                    width = 500
                )
            ),
            posters = listOf(
                ImageItemEntity(
                    aspectRatio = 0.67,
                    height = 1500,
                    iso6391 = "en",
                    fileUrl = "/poster1.jpg",
                    voteAverage = 9.0,
                    voteCount = 200,
                    width = 1000
                )
            )
        )

        val mockTvShowImagesWithPostersOnly = TvShowImagesEntity(
            backdrops = emptyList(),
            id = TV_SHOW_ID,
            logos = listOf(
                ImageItemEntity(
                    aspectRatio = 1.0,
                    height = 500,
                    iso6391 = null,
                    fileUrl = "/logo1.jpg",
                    voteAverage = 7.8,
                    voteCount = 50,
                    width = 500
                )
            ),
            posters = listOf(
                ImageItemEntity(
                    aspectRatio = 0.67,
                    height = 1500,
                    iso6391 = "en",
                    fileUrl = "/poster1.jpg",
                    voteAverage = 9.0,
                    voteCount = 200,
                    width = 1000
                )
            )
        )

        val mockTvShowImagesWithLogosOnly = TvShowImagesEntity(
            backdrops = emptyList(),
            id = TV_SHOW_ID,
            logos = listOf(
                ImageItemEntity(
                    aspectRatio = 1.0,
                    height = 500,
                    iso6391 = null,
                    fileUrl = "/logo1.jpg",
                    voteAverage = 7.8,
                    voteCount = 50,
                    width = 500
                )
            ),
            posters = emptyList()
        )

        val mockTvShowImagesEmpty = TvShowImagesEntity(
            backdrops = emptyList(),
            id = TV_SHOW_ID,
            logos = emptyList(),
            posters = emptyList()
        )

        val mockTvShowImagesWithManyBackdrops = TvShowImagesEntity(
            backdrops = List(15) { index ->
                ImageItemEntity(
                    aspectRatio = 1.78,
                    height = 1080,
                    iso6391 = "en",
                    fileUrl = "/backdrop$index.jpg",
                    voteAverage = 8.5,
                    voteCount = 100,
                    width = 1920
                )
            },
            id = TV_SHOW_ID,
            logos = emptyList(),
            posters = emptyList()
        )
    }
}