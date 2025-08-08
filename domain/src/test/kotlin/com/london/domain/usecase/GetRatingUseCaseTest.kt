package com.london.domain.usecase

import com.london.domain.entity.RatedMedia
import com.london.domain.repository.RatingRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetRatingUseCaseTest {

    private lateinit var useCase: GetRatingUseCase
    private lateinit var repository: RatingRepository

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = GetRatingUseCase(repository)
    }

    @Test
    fun `getAllRated returns sorted list by rating`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockRatedMedia

        // When
        val result = useCase.getAllRated()

        // Then
        assertEquals(3, result.size)
        assertTrue(result[0].rating >= result[1].rating)
        assertTrue(result[1].rating >= result[2].rating)
    }

    @Test
    fun `getRatedMovies returns only movies sorted by rating`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockRatedMedia

        // When
        val result = useCase.getRatedMovies()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.all { it.isMovie })
        assertTrue(result[0].rating >= result[1].rating)
        assertEquals("Movie 2", result[0].title) // rating = 9
        assertEquals("Movie 1", result[1].title) // rating = 8
    }

    @Test
    fun `getRatedTvShows returns only tv shows sorted by rating`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockRatedMediaWithTvShows

        // When
        val result = useCase.getRatedTvShows()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.all { !it.isMovie })
        assertTrue(result[0].rating >= result[1].rating)
        assertEquals("TV Show 2", result[0].title) // rating = 9
        assertEquals("TV Show 1", result[1].title) // rating = 7
    }

    @Test
    fun `getAllRated returns empty list when repository returns empty`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns emptyList()

        // When
        val result = useCase.getAllRated()

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `getRatedMovies returns empty list when no movies`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockOnlyTvShows

        // When
        val result = useCase.getRatedMovies()

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `getRatedTvShows returns empty list when no tv shows`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockOnlyMovies

        // When
        val result = useCase.getRatedTvShows()

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `getAllRated returns correct rating`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockSingleItem

        // When
        val result = useCase.getAllRated()

        // Then
        assertEquals(1, result.size)
        assertEquals(8, result[0].rating)
    }

    @Test
    fun `getAllRated sorts by rating descending`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockSortedItems

        // When
        val result = useCase.getAllRated()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0].rating >= result[1].rating)
    }

    companion object {
        private val mockRatedMedia = listOf(
            RatedMedia(
                id = 1,
                title = "Movie 1",
                posterPath = "/movie1.jpg",
                rating = 8,
                isMovie = true
            ),
            RatedMedia(
                id = 2,
                title = "TV Show 1",
                posterPath = "/tvshow1.jpg",
                rating = 7,
                isMovie = false
            ),
            RatedMedia(
                id = 3,
                title = "Movie 2",
                posterPath = "/movie2.jpg",
                rating = 9,
                isMovie = true
            )
        )

        private val mockRatedMediaWithTvShows = listOf(
            RatedMedia(
                id = 1,
                title = "Movie 1",
                posterPath = "/movie1.jpg",
                rating = 8,
                isMovie = true
            ),
            RatedMedia(
                id = 2,
                title = "TV Show 1",
                posterPath = "/tvshow1.jpg",
                rating = 7,
                isMovie = false
            ),
            RatedMedia(
                id = 3,
                title = "TV Show 2",
                posterPath = "/tvshow2.jpg",
                rating = 9,
                isMovie = false
            )
        )

        private val mockOnlyTvShows = listOf(
            RatedMedia(
                id = 1,
                title = "TV Show 1",
                posterPath = "/tvshow1.jpg",
                rating = 7,
                isMovie = false
            )
        )

        private val mockOnlyMovies = listOf(
            RatedMedia(
                id = 1,
                title = "Movie 1",
                posterPath = "/movie1.jpg",
                rating = 8,
                isMovie = true
            )
        )

        private val mockSingleItem = listOf(
            RatedMedia(
                id = 1,
                title = "Movie 1",
                posterPath = "/movie1.jpg",
                rating = 8,
                isMovie = true
            )
        )

        private val mockSortedItems = listOf(
            RatedMedia(
                id = 1,
                title = "First",
                posterPath = "/first.jpg",
                rating = 8,
                isMovie = true
            ),
            RatedMedia(
                id = 2,
                title = "Second",
                posterPath = "/second.jpg",
                rating = 7,
                isMovie = false
            )
        )
    }
} 