package com.london.domain.usecase.details.tvshow

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Actor
import com.london.domain.entity.ImagesEntity
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.entity.TvShow
import com.london.domain.entity.genre.TvShowGenre
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.review.AuthorDetails
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.repository.ActorRepository
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

class GetTvShowUseCaseTest {
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var searchRepository: SearchRepository
    private lateinit var getTvShowUseCase: GetTvShowUseCase

    private lateinit var actorRepository: ActorRepository

    @Before
    fun setUp() {
        tvShowRepository = mockk(relaxed = true)
        searchRepository = mockk(relaxed = true)
        actorRepository = mockk(relaxed = true)
        getTvShowUseCase = GetTvShowUseCase(
            tvShowRepository = tvShowRepository,
            searchRepository = searchRepository,
            actorRepository = actorRepository
        )
    }

    @Test
    fun `should return tv show details when repository returns tv show details`() = runTest {
        //given
        coEvery { tvShowRepository.getTvShowDetailsById(TV_SHOW_ID) } returns mockTvShowDetails
        //when
        val result = getTvShowUseCase.getTvShowDetails(TV_SHOW_ID)
        //then
        assertThat(result).isEqualTo(mockTvShowDetails)
    }
    // endregion

    // region GetPopular
    @Test
    fun `getPopular default limit should return 5 tv shows`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_FULL_LIST.map { createMockTvShow(it) }
        coEvery { tvShowRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = getTvShowUseCase.getPopularTvShows()

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
        val result = getTvShowUseCase.getPopularTvShows(CUSTOM_LIMIT)

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
            val result = getTvShowUseCase.getPopularTvShows(LARGE_LIMIT)

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
        val result = getTvShowUseCase.getPopularTvShows()

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
        val result = getTvShowUseCase.getPopularTvShows(ZERO_LIMIT)

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
            getTvShowUseCase.getPopularTvShows()
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
        val result = getTvShowUseCase.getPopularTvShows(1)

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
        val result = getTvShowUseCase.getPopularTvShows(CUSTOM_LIMIT)

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
        val result = getTvShowUseCase.getPopularTvShows()

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
            getTvShowUseCase.getTrendingTvShows(page = 1)
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
        coEvery {
            getTvShowUseCase.getTvSeasonTrailer(
                TV_SHOW_ID,
                SEASON_NUMBER
            )
        } returns mockVideos

        // when
        val result = getTvShowUseCase.getTvSeasonTrailer(TV_SHOW_ID, SEASON_NUMBER)

        // then
        assertThat(result).isNotEmpty()
    }

    @Test
    fun `should return empty list when repository returns no videos`() = runTest {
        // given
        coEvery {
            getTvShowUseCase.getTvSeasonTrailer(
                TV_SHOW_ID,
                SEASON_NUMBER
            )
        } returns emptyList()

        // when
        val result = getTvShowUseCase.getTvSeasonTrailer(TV_SHOW_ID, SEASON_NUMBER)

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
                getTvShowUseCase.getTvShowsByGenre(CATEGORY, PAGE_NUMBER)
            } returns pagedFetchResponse
            //when
            val result = getTvShowUseCase.getTvShowsByGenre(CATEGORY, PAGE_NUMBER)
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
                getTvShowUseCase.getTvShowList(NAME, PAGE_NUMBER)
            } returns pagedFetchResponse

            // When
            val result = getTvShowUseCase.getTvShowList(NAME, PAGE_NUMBER)

            // Then
            assertThat(result).isEqualTo(pagedFetchResponse)
        }


    @Test
    fun `should return TV series when repository returns valid response`() = runTest {
        // Given
        val mockPagedResponse = PagedFetchResponse(
            items = mockTopRatedTvSeries,
            currentPage = PAGE,
            totalPages = 2,
            totalItems = mockTopRatedTvSeries.size
        )

        coEvery {
            tvShowRepository.getTopRatedTvShows(PAGE)
        } returns mockPagedResponse

        // When
        val result = getTvShowUseCase.getAllTopRatedTvShows(PAGE)

        // Then
        assertThat(result).isEqualTo(mockPagedResponse)

    }

    @Test
    fun `should return empty list when repository returns empty response`() = runTest {
        // Given
        val emptyPagedResponse = PagedFetchResponse(
            items = emptyList<TopRatedMedia>(),
            totalPages = PAGE,
            currentPage = 1,
            totalItems = 0
        )

        coEvery {
            tvShowRepository.getTopRatedTvShows(PAGE)
        } returns emptyPagedResponse

        // When
        val result = getTvShowUseCase.getAllTopRatedTvShows(PAGE)

        // Then
        assertThat(result.items).isEmpty()
        assertThat(result.currentPage).isEqualTo(PAGE)
    }

    @Test
    fun `should throw RuntimeException when repository throws`() = runTest {
        // Given
        coEvery {
            tvShowRepository.getTopRatedTvShows(PAGE)
        } throws RuntimeException("Something went wrong")

        // When & Then
        assertThrows<RuntimeException> {
            getTvShowUseCase.getAllTopRatedTvShows(PAGE)
        }
    }

    @Test
    fun `should return tv show images when repository returns images`() = runTest {
        // Given
        val expectedImages = listOf("https://example.com/image1.jpg")
        coEvery { tvShowRepository.getImagesTvShowById(TV_SHOW_ID) } returns fakeMockImages(
            id = TV_SHOW_ID,
            backdropsUrl = listOf("https://example.com/image1.jpg"),
        )

        // When
        val result = getTvShowUseCase.getImagesTvShowById(TV_SHOW_ID)

        // Then
        assertThat(result).isEqualTo(expectedImages)
    }

    @Test
    fun `should return empty list when repository returns empty images`() = runTest {
        // Given
        coEvery { tvShowRepository.getImagesTvShowById(TV_SHOW_ID) } returns fakeMockImages()

        // When
        val result = getTvShowUseCase.getImagesTvShowById(TV_SHOW_ID)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return images in logosUrl when available`() = runTest {
        // Given
        val expectedImages = listOf("https://example.com/logos1.jpg")
        coEvery { tvShowRepository.getImagesTvShowById(TV_SHOW_ID) } returns fakeMockImages(
            logosUrl = listOf("https://example.com/logos1.jpg")
        )

        // When
        val result = getTvShowUseCase.getImagesTvShowById(TV_SHOW_ID)

        // Then
        assertThat(result).isEqualTo(expectedImages)
    }

    @Test
    fun `should return images in postersUrl when available`() = runTest {
        // Given
        val expectedImages = listOf("https://example.com/poster1.jpg")
        coEvery { tvShowRepository.getImagesTvShowById(TV_SHOW_ID) } returns fakeMockImages(
            postersUrl = listOf("https://example.com/poster1.jpg")
        )

        // When
        val result = getTvShowUseCase.getImagesTvShowById(TV_SHOW_ID)

        // Then
        assertThat(result).isEqualTo(expectedImages)
    }

    @Test
    fun `should return tv show reviews when repository returns reviews`() = runTest {
        // Given
        coEvery {
            tvShowRepository.getTvShowReviews(
                TV_SHOW_ID,
                PAGE_NUMBER
            )
        } returns pagedFetchReviewResponse()

        // When
        val result = getTvShowUseCase.getTvShowReviews(TV_SHOW_ID, PAGE_NUMBER)

        // Then
        assertThat(result).isEqualTo(pagedFetchReviewResponse())
    }

    @Test
    fun `should return recent tv shows when repository returns recent tv shows`() = runTest {
        // Given
        coEvery { tvShowRepository.getFirstPageTopRatedTvShows() } returns mockTopRatedTvSeries

        // When
        val result = getTvShowUseCase.getMostRecentTvShows()

        // Then
        assertThat(result).isEqualTo(mockTopRatedTvSeries)
    }

    companion object {
        private const val PAGE = 1
        private const val PAGE_NUMBER = 1
        private const val TV_SHOW_ID = 12345
        private const val CUSTOM_LIMIT = 3
        private const val LARGE_LIMIT = 10
        private const val ZERO_LIMIT = 0
        private const val EXCEPTION_MESSAGE = "Network error"
        private val CATEGORY = TvShowGenre.TALK
        private const val SEASON_NUMBER = 1
        private const val NAME = "Tv Tv"

        private fun pagedFetchReviewResponse(items: List<ReviewEntity> = mockReviews) =
            PagedFetchResponse(
                currentPage = 1,
                items = items,
                totalPages = 1,
                totalItems = items.size
            )

        private val mockReviews = listOf(
            ReviewEntity(
                id = "1",
                content = "Great show!",
                authorName = "John Doe",
                authorDetails = AuthorDetails(
                    "John Doe", "john_doe",
                    profileUrl = "https://example.com/john.jpg",
                    rating = 8.5
                ),
                createdAt = "2023-09-01"
            ),
            ReviewEntity(
                id = "2",
                content = "I loved it!",
                authorName = "Jane Smith",
                authorDetails = AuthorDetails(
                    "Jane Smith", "jane_smith",
                    profileUrl = "https://example.com/jane.jpg",
                    rating = 8.5
                ),
                createdAt = "2023-09-01"
            )
        )
        private val mockTv1 = TopRatedMedia(
            id = 1396,
            name = "Breaking Bad",
            posterUrl = "/ggFHVNu6YYI5L9pCfOacjizRGt.jpg",
            genres = listOf(TvShowGenre.TALK, TvShowGenre.TALK),
            mediaType = MediaType.TvShow,
        )

        private val mockTv2 = TopRatedMedia(
            id = 87108,
            name = "Chernobyl",
            posterUrl = "/hlLXt2tOPT6RRnjiUmoxyG1LTFi.jpg",
            genres = listOf(TvShowGenre.TALK, TvShowGenre.TALK),
            mediaType = MediaType.TvShow,
        )

        val mockTopRatedTvSeries = listOf(mockTv1, mockTv2)

        private val ACTOR = Actor(
            id = 1,
            name = "Tom Holland",
            profilePictureUrl = "",
            characterName = ""
        )

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
            genreIds: List<TvShowGenre> = listOf(TvShowGenre.TALK, TvShowGenre.TALK)
        ): Trending = Trending(
            id = id,
            title = title,
            posterPath = posterPath,
            genres = genreIds
        )

        val tvShow = TvShow(
            id = 1,
            name = "",
            posterPicture = "",
            releaseYear = 2024,
            rating = 8,
            genres = listOf(TvShowGenre.TALK, TvShowGenre.TALK, TvShowGenre.TALK)
        )

        val pagedFetchResponse = PagedFetchResponse(
            currentPage = 1, items = listOf(tvShow), totalPages = 1, totalItems = 1
        )

        val mockVideos = listOf(
            "https://youtube.com/vid1",
            "https://youtube.com/vid2"
        )

        private fun fakeMockImages(
            id: Int = 1,
            backdropsUrl: List<String> = emptyList(),
            logosUrl: List<String> = emptyList(),
            postersUrl: List<String> = emptyList()
        ) = ImagesEntity(
            backdropsUrl = backdropsUrl,
            id = id,
            logosUrl = logosUrl,
            postersUrl = postersUrl
        )

        val mockTvShowDetails = TvShowDetailsEntity(
            firstAirDate = "2020-01-01",
            tvShowGenres = listOf(TvShowGenre.TALK),
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