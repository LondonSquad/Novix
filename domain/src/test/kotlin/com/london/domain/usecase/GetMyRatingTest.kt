package com.london.domain.usecase

import com.london.domain.entity.RatedMedia
import com.london.domain.repository.myrating.MyRatingRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMyRatingTest {

    private lateinit var useCase: GetMyRating
    private lateinit var repository: MyRatingRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetMyRating(repository)
    }

    @Test
    fun `test getAllRated returns sorted list with timestamps`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockRatedMedia

        // When
        val result = useCase.getAllRated()

        // Then
        assertEquals(3, result.size)
        assertTrue(result[0].addedAt >= result[1].addedAt)
        assertTrue(result[1].addedAt >= result[2].addedAt)
    }

    @Test
    fun `test getRatedMovies returns only movies`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockRatedMedia

        // When
        val result = useCase.getRatedMovies()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.all { it.isMovie })
        assertEquals("Movie 1", result[0].title)
        assertEquals("Movie 2", result[1].title)
    }

    @Test
    fun `test getRatedTvShows returns only tv shows`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockRatedMediaWithTvShows

        // When
        val result = useCase.getRatedTvShows()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.all { !it.isMovie })
        assertEquals("TV Show 1", result[0].title)
        assertEquals("TV Show 2", result[1].title)
    }

    @Test
    fun `test getAllRated returns empty list when repository returns empty`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns emptyList()

        // When
        val result = useCase.getAllRated()

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `test getRatedMovies returns empty list when no movies`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockOnlyTvShows

        // When
        val result = useCase.getRatedMovies()

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `test getRatedTvShows returns empty list when no tv shows`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockOnlyMovies

        // When
        val result = useCase.getRatedTvShows()

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `test getAllRated assigns new timestamps`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockSingleItem

        // When
        val result = useCase.getAllRated()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0].addedAt > 1000L)
    }

    @Test
    fun `test sorting by addedAt descending`() = runTest {
        // Given
        coEvery { repository.getAllRatedMedia() } returns mockSortedItems

        // When
        val result = useCase.getAllRated()

        // Then
        assertEquals(2, result.size)
        assertTrue(result[0].addedAt >= result[1].addedAt)
    }

    companion object {
        private val mockRatedMedia = listOf(
            RatedMedia(
                id = 1,
                title = "Movie 1",
                posterPath = "/movie1.jpg",
                rating = 8,
                isMovie = true,
                addedAt = 1000L
            ),
            RatedMedia(
                id = 2,
                title = "TV Show 1",
                posterPath = "/tvshow1.jpg",
                rating = 7,
                isMovie = false,
                addedAt = 2000L
            ),
            RatedMedia(
                id = 3,
                title = "Movie 2",
                posterPath = "/movie2.jpg",
                rating = 9,
                isMovie = true,
                addedAt = 3000L
            )
        )

        private val mockRatedMediaWithTvShows = listOf(
            RatedMedia(
                id = 1,
                title = "Movie 1",
                posterPath = "/movie1.jpg",
                rating = 8,
                isMovie = true,
                addedAt = 1000L
            ),
            RatedMedia(
                id = 2,
                title = "TV Show 1",
                posterPath = "/tvshow1.jpg",
                rating = 7,
                isMovie = false,
                addedAt = 2000L
            ),
            RatedMedia(
                id = 3,
                title = "TV Show 2",
                posterPath = "/tvshow2.jpg",
                rating = 9,
                isMovie = false,
                addedAt = 3000L
            )
        )

        private val mockOnlyTvShows = listOf(
            RatedMedia(
                id = 1,
                title = "TV Show 1",
                posterPath = "/tvshow1.jpg",
                rating = 7,
                isMovie = false,
                addedAt = 1000L
            )
        )

        private val mockOnlyMovies = listOf(
            RatedMedia(
                id = 1,
                title = "Movie 1",
                posterPath = "/movie1.jpg",
                rating = 8,
                isMovie = true,
                addedAt = 1000L
            )
        )

        private val mockSingleItem = listOf(
            RatedMedia(
                id = 1,
                title = "Movie 1",
                posterPath = "/movie1.jpg",
                rating = 8,
                isMovie = true,
                addedAt = 1000L
            )
        )

        private val mockSortedItems = listOf(
            RatedMedia(
                id = 1,
                title = "First",
                posterPath = "/first.jpg",
                rating = 8,
                isMovie = true,
                addedAt = 1000L
            ),
            RatedMedia(
                id = 2,
                title = "Second",
                posterPath = "/second.jpg",
                rating = 7,
                isMovie = false,
                addedAt = 2000L
            )
        )
    }
} 