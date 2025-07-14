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
    fun `should return tv show images when repository returns tv show images`() = runTest {
        //given
        coEvery { detailsRepository.getImagesTvShowById(TV_SHOW_ID) } returns mockTvShowImages
        //when
        val result = getImagesById(TV_SHOW_ID)
        //then
        assertThat(result).isEqualTo(mockTvShowImages)
    }

    @Test
    fun `should throw exception when repository throws exception`() = runTest {
        //given
        coEvery { detailsRepository.getImagesTvShowById(TV_SHOW_ID) } throws GetImagesByIdFailedException()
        //when //then
        assertThrows<GetImagesByIdFailedException> {
            getImagesById(TV_SHOW_ID)
        }
    }

    private companion object {
        const val TV_SHOW_ID = 12345
        val mockTvShowImages = TvShowImagesEntity(
            backdrops = listOf(
                ImageItemEntity(
                    aspectRatio = 1.78,
                    height = 1080,
                    iso6391 = "en",
                    filePath = "/backdrop1.jpg",
                    voteAverage = 8.5,
                    voteCount = 100,
                    width = 1920
                )
            ),
            id = TV_SHOW_ID,
            logos = listOf(
                ImageItemEntity(
                    aspectRatio = 1.0,
                    height = 500,
                    iso6391 = null,
                    filePath = "/logo1.jpg",
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
                    filePath = "/poster1.jpg",
                    voteAverage = 9.0,
                    voteCount = 200,
                    width = 1000
                )
            )
        )
    }
}