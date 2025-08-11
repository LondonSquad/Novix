package com.london.domain.usecase.details.tvshow

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.entity.TvShow
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.entity.tvshowdetails.TvShowGenreEntity
import com.london.domain.error.TvShowDetailsSearchFailedException
import com.london.domain.error.TvShowSearchFailedException
import com.london.domain.repository.SearchRepository
import com.london.domain.repository.TvShowRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows

class ManageTvShowDetailsUseCaseTest {
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var searchRepository: SearchRepository
    private lateinit var manageTvShowDetailsUseCase: ManageTvShowDetailsUseCase

    @Before
    fun setUp() {
        tvShowRepository = mockk()
        searchRepository = mockk()
        manageTvShowDetailsUseCase = ManageTvShowDetailsUseCase(
            tvShowRepository = tvShowRepository,
            searchRepository = searchRepository,
        )
    }

    // region GetDetails
    @Test
    fun `should return tv show details when repository returns tv show details`() = runTest {
        //given
        coEvery { tvShowRepository.getTvShowDetailsById(TV_SHOW_ID) } returns mockTvShowDetails
        //when
        val result = manageTvShowDetailsUseCase.getTvShowDetails(TV_SHOW_ID)
        //then
        assertThat(result).isEqualTo(mockTvShowDetails)
    }

    @Test
    fun `should throw exception when repository throws exception`() = runTest {
        //given
        coEvery { tvShowRepository.getTvShowDetailsById(TV_SHOW_ID) } throws TvShowDetailsSearchFailedException()
        //when //then
        assertThrows<TvShowDetailsSearchFailedException> {
            manageTvShowDetailsUseCase.getTvShowDetails(TV_SHOW_ID)
        }
    }
    // endregion

    // region GetPopular
    @Test
    fun `getPopular default limit should return 5 tv shows`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_FULL_LIST.map { createMockTvShow(it) }
        coEvery { tvShowRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = manageTvShowDetailsUseCase.getPopularTvShows()

        // Then
        assertThat(result).hasSize(MOCK_TV_SHOWS_LIMITED.size)
        assertThat(result[0].name).isEqualTo(MOCK_TV_SHOWS_FULL_LIST[0].name)
        assertThat(result[4].name).isEqualTo(MOCK_TV_SHOWS_FULL_LIST[4].name)
        coVerify(exactly = 1) { tvShowRepository.getPopularTvShows() }
    }

    @Test
    fun `getPopular with custom limit should return specified number of tv shows`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_FULL_LIST.map { createMockTvShow(it) }
        coEvery { tvShowRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = manageTvShowDetailsUseCase.getPopularTvShows(CUSTOM_LIMIT)

        // Then
        assertThat(result).hasSize(CUSTOM_LIMIT)
        assertThat(result[0].name).isEqualTo(MOCK_TV_SHOWS_FULL_LIST[0].name)
        assertThat(result[2].name).isEqualTo(MOCK_TV_SHOWS_FULL_LIST[2].name)
        coVerify(exactly = 1) { tvShowRepository.getPopularTvShows() }
    }

    @Test
    fun `getPopular with limit greater than available shows should return all available shows`() =
        runTest {
            // Given
            val mockTvShows = MOCK_TV_SHOWS_LIMITED.map { createMockTvShow(it) }
            coEvery { tvShowRepository.getPopularTvShows() } returns mockTvShows

            // When
            val result = manageTvShowDetailsUseCase.getPopularTvShows(LARGE_LIMIT)

            // Then
            assertThat(result).hasSize(MOCK_TV_SHOWS_LIMITED.size)
            assertThat(result[0].name).isEqualTo(MOCK_TV_SHOWS_LIMITED[0].name)
            assertThat(result[1].name).isEqualTo(MOCK_TV_SHOWS_LIMITED[1].name)
            coVerify(exactly = 1) { tvShowRepository.getPopularTvShows() }
        }

    @Test
    fun `getPopular with empty repository should return empty list`() = runTest {
        // Given
        coEvery { tvShowRepository.getPopularTvShows() } returns EMPTY_TV_SHOWS_LIST

        // When
        val result = manageTvShowDetailsUseCase.getPopularTvShows()

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { tvShowRepository.getPopularTvShows() }
    }

    @Test
    fun `getPopular with zero limit should return empty list`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_LIMITED.map { createMockTvShow(it) }
        coEvery { tvShowRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = manageTvShowDetailsUseCase.getPopularTvShows(ZERO_LIMIT)

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { tvShowRepository.getPopularTvShows() }
    }

    @Test
    fun `getPopular should propagate repository exceptions`() = runTest {
        // Given
        val exception = RuntimeException(EXCEPTION_MESSAGE)
        coEvery { tvShowRepository.getPopularTvShows() } throws exception

        // When & Then
        try {
            manageTvShowDetailsUseCase.getPopularTvShows()
            assertThat(false).isTrue()
        } catch (e: RuntimeException) {
            assertThat(e.message).isEqualTo(EXCEPTION_MESSAGE)
        }

        coVerify(exactly = 1) { tvShowRepository.getPopularTvShows() }
    }

    @Test
    fun `getPopular should return tv shows with correct properties`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_FULL_LIST.map { createMockTvShow(it) }
        coEvery { tvShowRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = manageTvShowDetailsUseCase.getPopularTvShows(1)

        // Then
        assertThat(result).hasSize(1)

        val firstShow = result[0]
        val expectedShow = MOCK_TV_SHOWS_FULL_LIST[0]

        assertThat(firstShow.id).isEqualTo(expectedShow.id)
        assertThat(firstShow.name).isEqualTo(expectedShow.name)
        assertThat(firstShow.posterUrl).isEqualTo(expectedShow.posterUrl)
        assertThat(firstShow.rating).isEqualTo(expectedShow.rating)
    }

    @Test
    fun `getPopular should return tv shows in correct order`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_FULL_LIST.map { createMockTvShow(it) }
        coEvery { tvShowRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = manageTvShowDetailsUseCase.getPopularTvShows(CUSTOM_LIMIT)

        // Then
        assertThat(result).hasSize(CUSTOM_LIMIT)
        assertThat(result.map { it.name }).containsExactly(
            MOCK_TV_SHOWS_FULL_LIST[0].name,
            MOCK_TV_SHOWS_FULL_LIST[1].name,
            MOCK_TV_SHOWS_FULL_LIST[2].name
        ).inOrder()
    }

    @Test
    fun `getPopular should return tv shows with valid ratings`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_FULL_LIST.map { createMockTvShow(it) }
        coEvery { tvShowRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = manageTvShowDetailsUseCase.getPopularTvShows()

        // Then
        assertThat(result).isNotEmpty()
        result.forEach { tvShow ->
            assertThat(tvShow.rating).isGreaterThan(0.0)
            assertThat(tvShow.rating).isAtMost(10.0)
            assertThat(tvShow.id).isGreaterThan(0)
            assertThat(tvShow.name).isNotEmpty()
            assertThat(tvShow.posterUrl).isNotEmpty()
        }
    }
    // endregion

    // region Trending
    @Test
    fun `invoke should return trending tv shows from repository`() = runTest {
        // Given
        val mockResponse = createMockTrendingResponse()
        coEvery { tvShowRepository.getTrendingTvShows(any()) } returns mockResponse

        // When
        val result = tvShowRepository.getTrendingTvShows(page = 1)

        // Then
        assertThat(result).isNotNull()
    }

    @Test
    fun `invoke should handle empty response`() = runTest {
        val emptyResponse = PagedFetchResponse<Trending>(
            currentPage = 1,
            items = emptyList(),
            totalPages = 0,
            totalItems = 0
        )
        coEvery { tvShowRepository.getTrendingTvShows(any()) } returns emptyResponse

        val result = tvShowRepository.getTrendingTvShows(page = 1)

        assertThat(result.totalPages).isEqualTo(0)
    }

    @Test
    fun `invoke should handle repository error`() = runTest {
        coEvery { tvShowRepository.getTrendingTvShows(any()) } throws Exception(
            "Failed to fetch movie details"
        )

        val exception = assertThrows<Exception> {
            manageTvShowDetailsUseCase.getTrendingTvShows(page = 1)
        }

        assertEquals("Failed to fetch movie details", exception.message)
    }

    @Test
    fun `invoke should handle negative page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { tvShowRepository.getTrendingTvShows(any()) } returns mockResponse

        val result = tvShowRepository.getTrendingTvShows(page = -1)

        assertEquals(1, result.currentPage)
    }
    // endregion

    // region VideoProvider
    @Test
    fun `should return tv show videos when repository returns videos`() = runTest {
        // given
        coEvery { manageTvShowDetailsUseCase.getTvShowVideoProvider(TV_SHOW_ID) } returns mockVideos

        // when
        val result = manageTvShowDetailsUseCase.getTvShowVideoProvider(TV_SHOW_ID)

        // then
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `should return empty list when repository returns no videos`() = runTest {
        // given
        coEvery { manageTvShowDetailsUseCase.getTvShowVideoProvider(TV_SHOW_ID) } returns emptyList()

        // when
        val result = manageTvShowDetailsUseCase.getTvShowVideoProvider(TV_SHOW_ID)

        // then
        assertThat(result).isEmpty()
    }
    // endregion

    // region TvShowsByCategory
    @Test
    fun `should return a paged fetch response of tvShows when repository successfully fetches tvShows`() =
        runTest {
            //given
            coEvery {
                manageTvShowDetailsUseCase.getTvShowsByCategory(CATEGORY_ID, PAGE_NUMBER)
            } returns pagedFetchResponse
            //when
            val result = manageTvShowDetailsUseCase.getTvShowsByCategory(CATEGORY_ID, PAGE_NUMBER)
            //then
            assertThat(result).isEqualTo(pagedFetchResponse)
        }
    // endregion

    // region GetTvShowList
    @Test
    fun `should return paged fetch response when repository returns paged fetch response`() =
        runTest {
            // Given
            coEvery {
                manageTvShowDetailsUseCase.getTvShowList(NAME, PAGE_NUMBER)
            } returns pagedFetchResponse

            // When
            val result = manageTvShowDetailsUseCase.getTvShowList(NAME, PAGE_NUMBER)

            // Then
            assertThat(result).isEqualTo(pagedFetchResponse)
        }

    @Test
    fun `should throw TvShowSearchFailedException when repository throws TvShowSearchFailedException`() =
        runTest {
            //given
            coEvery {
                manageTvShowDetailsUseCase.getTvShowList(NAME, PAGE_NUMBER)
            } throws TvShowSearchFailedException()

            //when //then
            assertThrows<TvShowSearchFailedException> {
                manageTvShowDetailsUseCase.getTvShowList(NAME, PAGE_NUMBER)
            }
        }
    // endregion

    private fun createMockTvShow(mockData: MockPopularMedia): PopularMedia =
        PopularMedia(
            id = mockData.id,
            name = mockData.name,
            posterUrl = mockData.posterUrl,
            rating = mockData.rating,
            mediaType = MediaType.TvShow
        )


    private fun createMockTrendingResponse(): PagedFetchResponse<Trending> =
        PagedFetchResponse(
            currentPage = 1,
            items = listOf(createMockTrending()),
            totalPages = 10,
            totalItems = 100
        )

    private fun createMockTrending(
        id: Int = 1,
        title: String = "Test TV Show",
        posterPath: String = "test_poster.jpg",
        genreIds: List<Int> = listOf(18, 35)
    ): Trending = Trending(
        id = id,
        title = title,
        posterPath = posterPath,
        genreIds = genreIds
    )


    private companion object {
        private const val TV_SHOW_ID = 12345
        private const val CUSTOM_LIMIT = 3
        private const val LARGE_LIMIT = 10
        private const val ZERO_LIMIT = 0
        private const val EXCEPTION_MESSAGE = "Network error"
        private const val CATEGORY_ID = 1
        private const val PAGE_NUMBER = 1
        const val NAME = "Tv Tv"

        val tvShow = TvShow(
            id = 1,
            name = "",
            posterPicture = "",
            releaseYear = 2024,
            rating = 8,
            genres = listOf(1, 2, 3)
        )

        val pagedFetchResponse = PagedFetchResponse(
            currentPage = 1, items = listOf(tvShow), totalPages = 1, totalItems = 1
        )

        val mockVideos = listOf(
            "https://youtube.com/vid1",
            "https://youtube.com/vid2"
        )

        val mockTvShowDetails = TvShowDetailsEntity(
            firstAirDate = "2020-01-01",
            tvShowGenres = listOf(TvShowGenreEntity(id = 1, name = "Drama")),
            id = TV_SHOW_ID,
            name = "Test Show",
            numberOfEpisodes = 10,
            numberOfSeasons = 2,
            overview = "A test show overview",
            posterUrl = "/poster.jpg",
            tvShowSeasons = listOf(10),
            voteAverage = 8.5,
        )

        private val EMPTY_TV_SHOWS_LIST = emptyList<PopularMedia>()

        private val MOCK_TV_SHOWS_FULL_LIST = listOf(
            MockPopularMedia(
                id = 1,
                name = "Breaking Bad",
                overview = "A high school chemistry teacher turned methamphetamine manufacturer",
                posterUrl = "https://example.com/breaking-bad-poster.jpg",
                rating = 9.5,
            ),
            MockPopularMedia(
                id = 2,
                name = "Stranger Things",
                overview = "When a young boy disappears, his mother and friends must face terrifying supernatural forces",
                posterUrl = "https://example.com/stranger-things-poster.jpg",
                rating = 8.7
            ),
            MockPopularMedia(
                id = 3,
                name = "The Crown",
                overview = "Follows the political rivalries and romance of Queen Elizabeth II's reign",
                posterUrl = "https://example.com/the-crown-poster.jpg",
                rating = 8.6
            ),
            MockPopularMedia(
                id = 4,
                name = "Game of Thrones",
                overview = "Nine noble families fight for control over the lands of Westeros",
                posterUrl = "https://example.com/got-poster.jpg",
                rating = 9.2
            ),
            MockPopularMedia(
                id = 5,
                name = "The Office",
                overview = "A mockumentary on a group of typical office workers",
                posterUrl = "https://example.com/the-office-poster.jpg",
                rating = 8.9
            ),
            MockPopularMedia(
                id = 6,
                name = "Friends",
                overview = "Follows the personal and professional lives of six friends living in Manhattan",
                posterUrl = "https://example.com/friends-poster.jpg",
                rating = 8.8
            ),
            MockPopularMedia(
                id = 7,
                name = "The Mandalorian",
                overview = "A lone bounty hunter makes his way through the outer reaches of the galaxy",
                posterUrl = "https://example.com/mandalorian-poster.jpg",
                rating = 8.5
            )
        )

        private val MOCK_TV_SHOWS_LIMITED = listOf(
            MockPopularMedia(
                id = 1,
                name = "Breaking Bad",
                overview = "A high school chemistry teacher turned methamphetamine manufacturer",
                posterUrl = "https://example.com/breaking-bad-poster.jpg",
                rating = 9.5
            ),
            MockPopularMedia(
                id = 2,
                name = "Stranger Things",
                overview = "When a young boy disappears, his mother and friends must face terrifying supernatural forces",
                posterUrl = "https://example.com/stranger-things-poster.jpg",
                rating = 8.7
            ),
            MockPopularMedia(
                id = 2,
                name = "Stranger Things",
                overview = "When a young boy disappears, his mother and friends must face terrifying supernatural forces",
                posterUrl = "https://example.com/stranger-things-poster.jpg",
                rating = 8.7
            ),
            MockPopularMedia(
                id = 2,
                name = "Stranger Things",
                overview = "When a young boy disappears, his mother and friends must face terrifying supernatural forces",
                posterUrl = "https://example.com/stranger-things-poster.jpg",
                rating = 8.7
            ),
            MockPopularMedia(
                id = 2,
                name = "Stranger Things",
                overview = "When a young boy disappears, his mother and friends must face terrifying supernatural forces",
                posterUrl = "https://example.com/stranger-things-poster.jpg",
                rating = 8.7
            ),
        )

        data class MockPopularMedia(
            val id: Int,
            val name: String,
            val overview: String,
            val posterUrl: String,
            val rating: Double
        )
    }
}
