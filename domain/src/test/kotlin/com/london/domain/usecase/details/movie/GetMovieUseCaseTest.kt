package com.london.domain.usecase.details.movie

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.MovieImages
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows

class GetMovieUseCaseTest {
    private lateinit var movieRepository: MovieRepository
    private lateinit var actorRepository: ActorRepository
    private lateinit var getMovieUseCase: GetMovieUseCase

    @Before
    fun setUp() {
        movieRepository = mockk()
        getMovieUseCase = GetMovieUseCase(
            movieRepository = movieRepository,
            actorRepository = actorRepository
        )
    }

    // region Movie Details Tests
    @Test
    fun `getMovieDetails should return movie details successfully`() = runTest {
        // Given
        val movieId = 123
        val expectedMovie = fakeMovieDetailsDomain()
        coEvery { movieRepository.getMovieById(movieId) } returns expectedMovie

        // When
        val result = getMovieUseCase.getMovieDetails(movieId)

        // Then
        assertEquals(123, result.id)
        assertEquals("Inception", result.title)
        assertEquals("8.8", result.voteAverage)
        assertEquals(148, result.runtime)
        assertEquals("2010-07-16", result.releaseDate)
        assertEquals("A skilled thief is given a chance at redemption.", result.overview)
        assertEquals(3, result.genresId.size)

        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `getMovieDetails should propagate Exception when repository fails`() = runTest {
        // Given
        val movieId = 123
        val expectedException = Exception("Failed to fetch movie details")
        coEvery { movieRepository.getMovieById(movieId) } throws expectedException

        // When & Then
        val exception = assertThrows<Exception> {
            getMovieUseCase.getMovieDetails(movieId)
        }

        assertEquals("Failed to fetch movie details", exception.message)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `getMovieDetails should work with different movie IDs`() = runTest {
        // Given
        val movieId = 456
        val expectedMovie = fakeMovieDetailsDomain().copy(id = movieId, title = "Inception")
        coEvery { movieRepository.getMovieById(movieId) } returns expectedMovie

        // When
        val result = getMovieUseCase.getMovieDetails(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertEquals("Inception", result.title)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `getMovieDetails should handle movie with empty genres list`() = runTest {
        // Given
        val movieId = 789
        val movieWithNoGenres = fakeMovieDetailsDomain().copy(
            id = movieId, genresId = emptyList()
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithNoGenres

        // When
        val result = getMovieUseCase.getMovieDetails(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertTrue(result.genresId.isEmpty())
        assertEquals(0, result.genresId.size)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `getMovieDetails should handle movie with empty image list`() = runTest {
        // Given
        val movieId = 999
        val movieWithEmptyImages = fakeMovieDetailsDomain().copy(
            id = movieId, backdropUrl = ""
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithEmptyImages

        // When
        val result = getMovieUseCase.getMovieDetails(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertTrue(result.backdropUrl.isEmpty())
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `getMovieDetails should handle movie with zero runtime`() = runTest {
        // Given
        val movieId = 111
        val movieWithZeroRuntime = fakeMovieDetailsDomain().copy(
            id = movieId,
            runtime = 0
        )
        coEvery { movieRepository.getMovieById(movieId) } returns movieWithZeroRuntime

        // When
        val result = getMovieUseCase.getMovieDetails(movieId)

        // Then
        assertEquals(movieId, result.id)
        assertEquals(0, result.runtime)
        coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
    }

    @Test
    fun `getMovieDetails should not call repository multiple times for same invocation`() =
        runTest {
            // Given
            val movieId = 666
            val expectedMovie = fakeMovieDetailsDomain().copy(id = movieId)
            coEvery { movieRepository.getMovieById(movieId) } returns expectedMovie

            // When
            getMovieUseCase.getMovieDetails(movieId)

            // Then
            coVerify(exactly = 1) { movieRepository.getMovieById(movieId) }
        }
    //endregion

    // region Similar Movies Tests
    @Test
    fun `getSimilarMovies should return similar movies when repository returns data`() = runTest {
        // given
        coEvery { movieRepository.getSimilarMoviesById(MOVIE_ID) } returns similarMovieMockList

        // when
        val result = getMovieUseCase.getSimilarMovies(MOVIE_ID)

        // then
        assertThat(result).isEqualTo(similarMovieMockList)
        coVerify(exactly = 1) { movieRepository.getSimilarMoviesById(MOVIE_ID) }
    }

    @Test
    fun `getSimilarMovies should return empty list when repository returns empty list`() = runTest {
        // given
        coEvery { movieRepository.getSimilarMoviesById(MOVIE_ID) } returns emptyList()

        // when
        val result = getMovieUseCase.getSimilarMovies(MOVIE_ID)

        // then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { movieRepository.getSimilarMoviesById(MOVIE_ID) }
    }

    @Test
    fun `getSimilarMovies should call repository with correct movie ID`() = runTest {
        // given
        val customId = 999
        coEvery { movieRepository.getSimilarMoviesById(customId) } returns emptyList()

        // when
        getMovieUseCase.getSimilarMovies(customId)

        // then
        coVerify(exactly = 1) { movieRepository.getSimilarMoviesById(customId) }
    }

    @Test
    fun `getSimilarMovies should return different results for different movie IDs`() = runTest {
        // given
        val firstId = 111
        val secondId = 222

        val firstMovieList = listOf(createDummySimilarMovie(11, "Movie 11"))
        val secondMovieList = listOf(createDummySimilarMovie(20, "Movie 20"))

        coEvery { movieRepository.getSimilarMoviesById(firstId) } returns firstMovieList
        coEvery { movieRepository.getSimilarMoviesById(secondId) } returns secondMovieList

        // when
        val resultForFirstId = getMovieUseCase.getSimilarMovies(firstId)
        val resultForSecondId = getMovieUseCase.getSimilarMovies(secondId)

        // then
        assertThat(resultForFirstId).containsExactlyElementsIn(firstMovieList)
        assertThat(resultForSecondId).containsExactlyElementsIn(secondMovieList)
    }
    //endregion

    // region Movie Images Tests
    @Test
    fun `getFirstTenMovieImagesUseCase should return movie images when repository returns data`() =
        runTest {
            // given
            coEvery { movieRepository.getMovieImagesById(MOVIE_ID) } returns movieImages
            // when
            val result = getMovieUseCase.getMovieImagesUseCase(MOVIE_ID)
            // then
            assertThat(result).isEqualTo(movieMockImages)
        }

    @Test
    fun `getFirstTenMovieImagesUseCase should return empty list when repository returns empty list`() =
        runTest {
            // given
            coEvery { movieRepository.getMovieImagesById(MOVIE_ID) } returns MovieImages(
                backdrops = emptyList(),
                id = MOVIE_ID,
                logos = emptyList(),
                posters = emptyList()
            )
            // when
            val result = getMovieUseCase.getMovieImagesUseCase(MOVIE_ID)
            // then
            assertThat(result).isEmpty()
        }

    @Test
    fun `getFirstTenMovieImagesUseCase should limit the number of images returned when images over 10`() =
        runTest {
            // given
            val manyImages = (1..15).map { "/images/movie$it.jpg" }
            coEvery { movieRepository.getMovieImagesById(MOVIE_ID) } returns MovieImages(
                backdrops = manyImages,
                id = MOVIE_ID,
                logos = emptyList(),
                posters = emptyList()
            )

            // when
            val result = getMovieUseCase.getMovieImagesUseCase(MOVIE_ID)

            // then
            assertThat(result).hasSize(10)
        }
    //endregion

    // region Movie Cast Tests
    @Test
    fun `getMovieCast should return cast when repository returns cast`() = runTest {
        // given
        coEvery { actorRepository.getMovieCastById(MOVIE_ID) } returns actorMockCast

        // when
        val result = getMovieUseCase.getMovieCast(MOVIE_ID)

        // then
        assertThat(result).isEqualTo(actorMockCast)
    }

    @Test
    fun `getMovieCast should return empty list when repository returns empty list`() = runTest {
        // given
        coEvery { actorRepository.getMovieCastById(MOVIE_ID) } returns emptyList()

        // when
        val result = getMovieUseCase.getMovieCast(MOVIE_ID)

        // then
        assertThat(result).isEmpty()
        coVerify(exactly = 1) { actorRepository.getMovieCastById(MOVIE_ID) }
    }

    @Test
    fun `getMovieCast should call repository with correct movie ID`() = runTest {
        // given
        val customId = 999
        coEvery { actorRepository.getMovieCastById(customId) } returns emptyList()

        // when
        getMovieUseCase.getMovieCast(customId)

        // then
        coVerify(exactly = 1) { actorRepository.getMovieCastById(customId) }
    }

    @Test
    fun `getMovieCast should return different results for different movie IDs`() = runTest {
        // given
        val actorCast = listOf(
            Actor(
                id = 3,
                name = "Tom Hardy",
                characterName = "Eames",
                profilePictureUrl = "/hardy.jpg"
            )
        )

        coEvery { actorRepository.getMovieCastById(123) } returns actorMockCast
        coEvery { actorRepository.getMovieCastById(456) } returns actorCast

        // when
        val result1 = getMovieUseCase.getMovieCast(123)
        val result2 = getMovieUseCase.getMovieCast(456)

        // then
        assertThat(result1).hasSize(2)
        assertThat(result2).containsExactlyElementsIn(actorCast)
    }
    // endregion

    // region Movie Video Tests
    @Test
    fun `getMovieVideo should return movie videos when repository returns videos`() = runTest {
        // given
        coEvery { movieRepository.getMovieVideos(MOVIE_ID) } returns mockVideos

        // when
        val result = getMovieUseCase.getMovieVideo(MOVIE_ID)

        // then
        assertThat(result[0]).isEqualTo(mockVideos[0])
        assertThat(result[1]).isEqualTo(mockVideos[1])
    }

    @Test
    fun `getMovieVideo should return empty list when repository returns no videos`() = runTest {
        // given
        coEvery { movieRepository.getMovieVideos(MOVIE_ID) } returns emptyList()

        // when
        val result = getMovieUseCase.getMovieVideo(MOVIE_ID)

        // then
        assertThat(result).isEmpty()
    }

    @Test
    fun `getMovieVideo should throw exception when repository throws exception`() = runTest {
        // given
        coEvery { movieRepository.getMovieVideos(MOVIE_ID) } throws RuntimeException("Network error")

        // when // then
        assertThrows<RuntimeException> {
            getMovieUseCase.getMovieVideo(MOVIE_ID)
        }
    }

    @Test
    fun `when call invoke should return list of popular movies from repository`() = runTest {
        // Given
        val expectedMovies = listOf(
            PopularMedia(
                id = 1,
                name = "Movie One",
                posterUrl = "path1",
                rating = 7.5,
                mediaType = MediaType.Movie
            ),
            PopularMedia(
                id = 2,
                name = "Movie Two",
                rating = 8.3,
                posterUrl = "path2",
                mediaType = MediaType.Movie
            )
        )
        coEvery { movieRepository.getPopularMovies() } returns expectedMovies

        // When
        val result = getMovieUseCase.getPopularMovies()

        // Then
        assertThat(result).isEqualTo(expectedMovies)
        coVerify(exactly = 1) { getMovieUseCase.getPopularMovies() }
    }

    @Test
    fun `invoke should return trending movies from repository`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { movieRepository.getTrendingMovies(any()) } returns mockResponse

        val result = getMovieUseCase.getTrendingMovies(page = 1)

        Assert.assertNotNull(result)
        Assert.assertEquals(1, result.currentPage)
        Assert.assertEquals(10, result.totalPages)
        Assert.assertEquals(100, result.totalItems)
        Assert.assertEquals(1, result.items.size)

        val trending = result.items.first()
        Assert.assertEquals(1, trending.id)
        Assert.assertEquals("Test Movie", trending.title)
        Assert.assertEquals("test_poster.jpg", trending.posterPath)
        Assert.assertEquals(listOf(28, 12), trending.genreIds)
    }

    @Test
    fun `invoke should handle different page numbers`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { movieRepository.getTrendingMovies(any()) } returns mockResponse

        val result1 = getMovieUseCase.getTrendingMovies(page = 1)
        val result2 = getMovieUseCase.getTrendingMovies(page = 2)

        Assert.assertNotNull(result1)
        Assert.assertNotNull(result2)
        Assert.assertEquals(1, result1.currentPage)
        Assert.assertEquals(1, result2.currentPage)
    }

    @Test
    fun `invoke should handle empty response`() = runTest {
        val emptyResponse = PagedFetchResponse<Trending>(
            currentPage = 1,
            items = emptyList(),
            totalPages = 0,
            totalItems = 0
        )
        coEvery { movieRepository.getTrendingMovies(any()) } returns emptyResponse

        val result = getMovieUseCase.getTrendingMovies(page = 1)

        Assert.assertNotNull(result)
        Assert.assertEquals(1, result.currentPage)
        Assert.assertEquals(0, result.totalPages)
        Assert.assertEquals(0, result.totalItems)
        Assert.assertEquals(0, result.items.size)
    }

    @Test
    fun `invoke should handle multiple movies in response`() = runTest {
        val multipleMoviesResponse = PagedFetchResponse(
            currentPage = 1,
            items = listOf(
                createMockTrending(id = 1, title = "Movie 1"),
                createMockTrending(id = 2, title = "Movie 2"),
                createMockTrending(id = 3, title = "Movie 3")
            ),
            totalPages = 10,
            totalItems = 100
        )
        coEvery { movieRepository.getTrendingMovies(any()) } returns multipleMoviesResponse

        val result = getMovieUseCase.getTrendingMovies(page = 1)

        Assert.assertNotNull(result)
        Assert.assertEquals(3, result.items.size)
        Assert.assertEquals("Movie 1", result.items[0].title)
        Assert.assertEquals("Movie 2", result.items[1].title)
        Assert.assertEquals("Movie 3", result.items[2].title)
    }

    @Test
    fun `invoke should handle repository error`() = runTest {
        val error = Exception("Repository error")
        coEvery { movieRepository.getTrendingMovies(any()) } throws error

        try {
            getMovieUseCase.getTrendingMovies(page = 1)
            assert(false)
        } catch (e: Exception) {
            Assert.assertEquals("Repository error", e.message)
        }
    }

    @Test
    fun `invoke should handle negative page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { movieRepository.getTrendingMovies(any()) } returns mockResponse

        val result = getMovieUseCase.getTrendingMovies(page = -1)

        Assert.assertNotNull(result)
        Assert.assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle zero page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { movieRepository.getTrendingMovies(any()) } returns mockResponse

        val result = getMovieUseCase.getTrendingMovies(page = 0)

        Assert.assertNotNull(result)
        Assert.assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle large page number`() = runTest {
        val mockResponse = createMockTrendingResponse()
        coEvery { movieRepository.getTrendingMovies(any()) } returns mockResponse

        val result = getMovieUseCase.getTrendingMovies(page = 999)

        Assert.assertNotNull(result)
        Assert.assertEquals(1, result.currentPage)
    }


    @Test
    fun `should return a paged fetch response of movies when repository successfully fetches movies`() =
        runTest {
            //given
            coEvery {
                movieRepository.getMoviesByCategory(
                    CATEGORY_ID, PAGE_NUMBER
                )
            } returns pagedFetchResponse
            //when
            val result = getMovieUseCase.getMoviesByCategory(CATEGORY_ID, PAGE_NUMBER)
            //then
            assertThat(result).isEqualTo(pagedFetchResponse)
        }

    @Test
    fun `should return movies when repository returns valid response`() = runTest {
        // Given
        val mockPagedResponse = PagedFetchResponse(
            items = mockTopRatedMovies,
            currentPage = PAGE,
            totalPages = TOTAL_PAGES,
            totalItems = TOTAL_ITEMS

        )

        coEvery {
            movieRepository.getTopRatedMovies(PAGE)
        } returns mockPagedResponse

        // When
        val result = getMovieUseCase.getTopRatedMovies(PAGE)

        // Then
        assertThat(result).isEqualTo(mockPagedResponse)
        assertThat(result.items[0].name).isEqualTo("The Shawshank Redemption")
        assertThat(result.items[1].name).isEqualTo("The Godfather")
    }

    @Test
    fun `should return empty list when repository returns empty response`() = runTest {
        // Given
        val emptyPagedResponse = PagedFetchResponse(
            currentPage = PAGE,
            totalPages = TOTAL_PAGES,
            totalItems = TOTAL_PAGES,
            items = emptyList<TopRatedMedia>()
        )

        coEvery {
            movieRepository.getTopRatedMovies(PAGE)
        } returns emptyPagedResponse

        // When
        val result = getMovieUseCase.getTopRatedMovies(PAGE)

        // Then
        assertThat(result.items).isEmpty()
    }

    @Test
    fun `should throw RuntimeException when repository throws`() = runTest {
        // Given
        coEvery {
            movieRepository.getTopRatedMovies(PAGE)
        } throws RuntimeException()

        // When & Then
        assertThrows<RuntimeException> {
            getMovieUseCase.getTopRatedMovies(PAGE)
        }
    }


    private companion object {
        private const val CATEGORY_ID = 1
        private const val PAGE_NUMBER = 1
        private const val PAGE = 1
        private const val TOTAL_PAGES = 2
        private const val TOTAL_ITEMS = 100
        private const val MOVIE_ID = 123


        private val mockMovie1 = TopRatedMedia(
            id = 278,
            name = "The Shawshank Redemption",
            voteAverage = 8.712,
            releaseDate = "1994-09-23",
            posterUrl = "/q6y0Go1tsGEsmtFryDOJo3dEmqu.jpg",
            genreIds = listOf(18, 80),
        )

        private val mockMovie2 = TopRatedMedia(
            id = 238,
            name = "The Godfather",
            voteAverage = 8.7, releaseDate = "1972-03-14",
            posterUrl = "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
            genreIds = listOf(18, 80),
        )

        val mockTopRatedMovies = listOf(mockMovie1, mockMovie2)

        private val movie = Movie(
            id = 1,
            name = "",
            posterUrl = "",
            releaseYear = 2024,
            rating = 8,
            genreIds = listOf(1, 2, 3)
        )
        private val pagedFetchResponse = PagedFetchResponse(
            currentPage = 1, items = listOf(movie), totalPages = 1, totalItems = 1
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
            title: String = "Test Movie",
            posterPath: String = "test_poster.jpg",
            genreIds: List<Int> = listOf(28, 12)
        ): Trending {
            return Trending(
                id = id,
                title = title,
                posterPath = posterPath,
                genreIds = genreIds
            )
        }

        private fun fakeMovieDetailsDomain() = MovieDetails(
            backdropUrl = "/inception_backdrop.jpg",
            genresId = listOf(1, 2, 3),
            id = 123,
            overview = "A skilled thief is given a chance at redemption.",
            posterUrl = "/inception_poster.jpg",
            releaseDate = "2010-07-16",
            runtime = 148,
            title = "Inception",
            video = false,
            voteAverage = "8.8",
        )

        private fun createDummySimilarMovie(id: Int, title: String) = Movie(
            id = id,
            name = title,
            posterUrl = "/backdrop_$id.jpg",
            genreIds = listOf(1, 2, 3),
            releaseYear = 2025,
            rating = 7,
        )

        private val similarMovieMockList = listOf(
            createDummySimilarMovie(1, "Similar Movie 1"),
            createDummySimilarMovie(2, "Similar Movie 2")
        )

        val movieMockImages = listOf(
            "/images/movie1.jpg",
            "/images/movie2.jpg",
            "/images/movie3.jpg",
            "/images/movie4.jpg",
            "/images/movie5.jpg",
            "/images/movie6.jpg",
            "/images/movie8.jpg",
            "/images/movie9.jpg",
            "/images/movie10.jpg",
            "/images/movie11.jpg",
        )

        val movieImages = MovieImages(
            backdrops = movieMockImages,
            posters = emptyList(),
            id = 0,
            logos = emptyList()
        )

        val actorMockCast = listOf(
            Actor(
                id = 1,
                name = "Leonardo DiCaprio",
                characterName = "Cobb",
                profilePictureUrl = "/leo.jpg"
            ),
            Actor(
                id = 2,
                name = "Joseph Gordon-Levitt",
                characterName = "Arthur",
                profilePictureUrl = "/jgl.jpg"
            )
        )

        val mockVideos = listOf(
            "https://youtube.com/watch?v=123",
            "https://youtube.com/watch?v=456",
        )
    }
}