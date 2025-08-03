package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.popular.PopularTvShow
import com.london.domain.repository.PopularRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPopularTvShowTest {

    private lateinit var popularRepository: PopularRepository
    private lateinit var getPopularTvShow: GetPopularTvShow

    @Before
    fun setUp() {
        popularRepository = mockk()
        getPopularTvShow = GetPopularTvShow(popularRepository)
    }

    @Test
    fun `invoke with default limit should return 5 tv shows`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_FULL_LIST.map { createMockTvShow(it) }
        coEvery { popularRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = getPopularTvShow.invoke()

        // Then
        assertThat(result).hasSize(GetPopularTvShow.LIMIT)
        assertThat(result[0].name).isEqualTo(MOCK_TV_SHOWS_FULL_LIST[0].name)
        assertThat(result[4].name).isEqualTo(MOCK_TV_SHOWS_FULL_LIST[4].name)
        coVerify(exactly = 1) { popularRepository.getPopularTvShows() }
    }

    @Test
    fun `invoke with custom limit should return specified number of tv shows`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_FULL_LIST.map { createMockTvShow(it) }
        coEvery { popularRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = getPopularTvShow.invoke(CUSTOM_LIMIT)

        // Then
        assertThat(result).hasSize(CUSTOM_LIMIT)
        assertThat(result[0].name).isEqualTo(MOCK_TV_SHOWS_FULL_LIST[0].name)
        assertThat(result[2].name).isEqualTo(MOCK_TV_SHOWS_FULL_LIST[2].name)
        coVerify(exactly = 1) { popularRepository.getPopularTvShows() }
    }

    @Test
    fun `invoke with limit greater than available shows should return all available shows`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_LIMITED.map { createMockTvShow(it) }
        coEvery { popularRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = getPopularTvShow.invoke(LARGE_LIMIT)

        // Then
        assertThat(result).hasSize(MOCK_TV_SHOWS_LIMITED.size)
        assertThat(result[0].name).isEqualTo(MOCK_TV_SHOWS_LIMITED[0].name)
        assertThat(result[1].name).isEqualTo(MOCK_TV_SHOWS_LIMITED[1].name)
        coVerify(exactly = 1) { popularRepository.getPopularTvShows() }
    }

    @Test
    fun `invoke with empty repository should return empty list`() = runTest {
        // Given
        coEvery { popularRepository.getPopularTvShows() } returns EMPTY_TV_SHOWS_LIST

        // When
        val result = getPopularTvShow.invoke()

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { popularRepository.getPopularTvShows() }
    }

    @Test
    fun `invoke with zero limit should return empty list`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_LIMITED.map { createMockTvShow(it) }
        coEvery { popularRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = getPopularTvShow.invoke(ZERO_LIMIT)

        // Then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { popularRepository.getPopularTvShows() }
    }

    @Test
    fun `invoke should propagate repository exceptions`() = runTest {
        // Given
        val exception = RuntimeException(EXCEPTION_MESSAGE)
        coEvery { popularRepository.getPopularTvShows() } throws exception

        // When & Then
        try {
            getPopularTvShow.invoke()
            assertThat(false).isTrue()
        } catch (e: RuntimeException) {
            assertThat(e.message).isEqualTo(EXCEPTION_MESSAGE)
        }

        coVerify(exactly = 1) { popularRepository.getPopularTvShows() }
    }

    @Test
    fun `invoke should return tv shows with correct properties`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_FULL_LIST.map { createMockTvShow(it) }
        coEvery { popularRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = getPopularTvShow.invoke(1)

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
    fun `invoke should return tv shows in correct order`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_FULL_LIST.map { createMockTvShow(it) }
        coEvery { popularRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = getPopularTvShow.invoke(CUSTOM_LIMIT)

        // Then
        assertThat(result).hasSize(CUSTOM_LIMIT)
        assertThat(result.map { it.name }).containsExactly(
            MOCK_TV_SHOWS_FULL_LIST[0].name,
            MOCK_TV_SHOWS_FULL_LIST[1].name,
            MOCK_TV_SHOWS_FULL_LIST[2].name
        ).inOrder()
    }

    @Test
    fun `invoke should return tv shows with valid ratings`() = runTest {
        // Given
        val mockTvShows = MOCK_TV_SHOWS_FULL_LIST.map { createMockTvShow(it) }
        coEvery { popularRepository.getPopularTvShows() } returns mockTvShows

        // When
        val result = getPopularTvShow.invoke()

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

    private fun createMockTvShow(mockData: MockTvShowData): PopularTvShow {
        return PopularTvShow(
            id = mockData.id,
            name = mockData.name,
            posterUrl = mockData.posterUrl,
            rating = mockData.rating
        )
    }

    companion object {

        private const val CUSTOM_LIMIT = 3
        private const val LARGE_LIMIT = 10
        private const val ZERO_LIMIT = 0
        private const val EXCEPTION_MESSAGE = "Network error"

        private val EMPTY_TV_SHOWS_LIST = emptyList<PopularTvShow>()

        private val MOCK_TV_SHOWS_FULL_LIST = listOf(
            MockTvShowData(
                id = 1,
                name = "Breaking Bad",
                overview = "A high school chemistry teacher turned methamphetamine manufacturer",
                posterUrl = "https://example.com/breaking-bad-poster.jpg",
                rating = 9.5
            ),
            MockTvShowData(
                id = 2,
                name = "Stranger Things",
                overview = "When a young boy disappears, his mother and friends must face terrifying supernatural forces",
                posterUrl = "https://example.com/stranger-things-poster.jpg",
                rating = 8.7
            ),
            MockTvShowData(
                id = 3,
                name = "The Crown",
                overview = "Follows the political rivalries and romance of Queen Elizabeth II's reign",
                posterUrl = "https://example.com/the-crown-poster.jpg",
                rating = 8.6
            ),
            MockTvShowData(
                id = 4,
                name = "Game of Thrones",
                overview = "Nine noble families fight for control over the lands of Westeros",
                posterUrl = "https://example.com/got-poster.jpg",
                rating = 9.2
            ),
            MockTvShowData(
                id = 5,
                name = "The Office",
                overview = "A mockumentary on a group of typical office workers",
                posterUrl = "https://example.com/the-office-poster.jpg",
                rating = 8.9
            ),
            MockTvShowData(
                id = 6,
                name = "Friends",
                overview = "Follows the personal and professional lives of six friends living in Manhattan",
                posterUrl = "https://example.com/friends-poster.jpg",
                rating = 8.8
            ),
            MockTvShowData(
                id = 7,
                name = "The Mandalorian",
                overview = "A lone bounty hunter makes his way through the outer reaches of the galaxy",
                posterUrl = "https://example.com/mandalorian-poster.jpg",
                rating = 8.5
            )
        )

        private val MOCK_TV_SHOWS_LIMITED = listOf(
            MockTvShowData(
                id = 1,
                name = "Breaking Bad",
                overview = "A high school chemistry teacher turned methamphetamine manufacturer",
                posterUrl = "https://example.com/breaking-bad-poster.jpg",
                rating = 9.5
            ),
            MockTvShowData(
                id = 2,
                name = "Stranger Things",
                overview = "When a young boy disappears, his mother and friends must face terrifying supernatural forces",
                posterUrl = "https://example.com/stranger-things-poster.jpg",
                rating = 8.7
            )
        )

        data class MockTvShowData(
            val id: Int,
            val name: String,
            val overview: String,
            val posterUrl: String,
            val rating: Double
        )
    }
}