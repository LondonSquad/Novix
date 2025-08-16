package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.local.model.home.topRated.TopRatedLocal
import com.london.data.local.preference.AuthenticationPreferences
import com.london.data.local.source.home.HomeLocalDataSource
import com.london.data.local.source.home.upcoming.UpComingLocalDataSource
import com.london.data.mapper.details.movie.toEntity
import com.london.data.mapper.search.toAuthorDetails
import com.london.data.remote.exception.NetworkException
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.ImageItem
import com.london.data.remote.model.details.movie.model.moviedetails.GenreRemote
import com.london.data.remote.model.details.movie.model.moviedetails.MovieDetailsResponse
import com.london.data.remote.model.details.movie.model.movieimages.MovieImagesResponse
import com.london.data.remote.model.details.rating.AccountStatesResponse
import com.london.data.remote.model.details.rating.RatingRemoteResponse
import com.london.data.remote.model.home.popular.PopularMovieResponse
import com.london.data.remote.model.home.trending.TrendingResponse
import com.london.data.remote.model.reviews.AuthorDetailsResponse
import com.london.data.remote.model.reviews.ReviewResponse
import com.london.data.remote.model.search.MovieRemote
import com.london.data.remote.model.search.SearchTvShowRemote
import com.london.data.remote.source.movie.MovieRemoteDataSource
import com.london.data.repository.movie.MovieRepositoryImpl
import com.london.data.utils.CrashReporter
import com.london.data.utils.asImageUrlOrEmpty
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.TvShow
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.moviedatails.MovieImages
import com.london.domain.entity.recent.MediaType
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows

class MovieRepositoryImplTest {

    private val authenticationPreferences: AuthenticationPreferences = mockk(relaxed = true)
    private val movieRemoteDataSource: MovieRemoteDataSource = mockk(relaxed = true)
    private val crashReporter: CrashReporter = mockk(relaxed = true)
    private val upComingLocalDataSource: UpComingLocalDataSource = mockk(relaxed = true)
    private val localTopRated: HomeLocalDataSource<TopRatedLocal> = mockk(relaxed = true)
    private val homeLocalDataSource: HomeLocalDataSource<PopularSectionLocal> =
        mockk(relaxed = true)
    private lateinit var repository: MovieRepository


    @Before
    fun setup() {
        repository = MovieRepositoryImpl(
            movieRemoteDataSource = movieRemoteDataSource,
            authenticationPreferences = authenticationPreferences,
            crashReporter = crashReporter,
            upComingLocalDataSource = upComingLocalDataSource,
            localTopRated = localTopRated,
            homeLocalDataSource = homeLocalDataSource
        )
    }

    @Test
    fun `getMovieUsingId should map remote data correctly`() = runTest {
        coEvery { movieRemoteDataSource.getMovieDetails(123) } returns Result.success(
            fakeMovieDetailsRemote()
        )
        coEvery { movieRemoteDataSource.getMovieImages(123) } returns Result.success(
            fakeMovieImagesRemote()
        )
        coEvery { movieRemoteDataSource.getSimilarMovies(123) } returns Result.success(
            fakeSimilarMoviesRemote()
        )

        val result = repository.getMovieById(123)

        assertEquals("Inception", result.title)
        assertEquals(2, result.genres.size)
    }

    @Test
    fun `getSimilarMovies should map similar movies correctly`() = runTest {
        coEvery { movieRemoteDataSource.getSimilarMovies(123) } returns Result.success(
            fakeSimilarMoviesRemote()
        )

        val result = repository.getSimilarMoviesById(123)

        assertEquals(2, result.size)
    }

    @Test
    fun `getMovieImages should return poster file paths`() = runTest {
        val expected = MovieImages(
            backdrops = emptyList(),
            id = 123,
            logos = emptyList(),
            posters = listOf(
                "/img1.jpg".asImageUrlOrEmpty(),
                "/img2.jpg".asImageUrlOrEmpty()
            )
        )

        coEvery { movieRemoteDataSource.getMovieImages(123) } returns Result.success(
            fakeMovieImagesRemote()
        )

        val result = repository.getMovieImagesById(123)

        assertEquals(expected, result)
    }

    @Test
    fun `getMovieCast should throw UnAuthorizedException when remote fails`() = runTest {
        coEvery { movieRemoteDataSource.getMovieDetails(123) } throws
                NetworkException.UnAuthorizedException(
                    message = "unauthorized",
                    status = 401
                )

        assertThrows<NetworkException.UnAuthorizedException> {
            repository.getMovieById(123)
        }
    }

    @Test
    fun `getMovieImages should throw HttpLockedException when remote fails`() = runTest {
        coEvery { movieRemoteDataSource.getMovieImages(123) } throws
                NetworkException.HttpLockedException(
                    "locked",
                    status = 423
                )

        assertThrows<NetworkException.HttpLockedException> {
            repository.getMovieImagesById(123)
        }
    }

    @Test
    fun `getSimilarMovies should throw TimeoutException when remote fails`() = runTest {
        coEvery { movieRemoteDataSource.getSimilarMovies(123) } throws
                NetworkException.TimeoutException(
                    message = "timeout",
                    status = 408
                )

        assertThrows<NetworkException.TimeoutException> {
            repository.getSimilarMoviesById(123)
        }
    }

    @Test
    fun `getMovieReviews should return paged reviews when remote succeeds`() = runTest {
        // Given
        val fakeRemoteResponse = ApiResponse(
            currentPage = 1, items = listOf(
                ReviewResponse(
                    id = "review1",
                    author = "Author 1",
                    content = "This is review 1",
                    createdAt = "2024-01-01",
                    authorDetailsResponse = AuthorDetailsResponse(
                        authorName = "John Doe",
                        authorUsername = "johndoe",
                        authorPictureUrl = "https://image.tmdb.org/t/p/w500/profile.jpg",
                        rating = 4.5
                    ),
                )
            ), totalPages = 1, totalItems = 1
        )
        coEvery { movieRemoteDataSource.getMovieReviews(MOVIE_ID, PAGE_NUMBER) }.returns(
            Result.success(fakeRemoteResponse)
        )

        // When
        val result: PagedFetchResponse<ReviewEntity> =
            repository.getMovieReviews(MOVIE_ID, PAGE_NUMBER)

        //Then
        assertThat(result.items.first().authorDetails).isEqualTo(fakeRemoteResponse.items.first().authorDetailsResponse.toAuthorDetails())
    }

    @Test
    fun `getMovieReviews should throw when remote fails`() = runTest {
        //Given
        coEvery { movieRemoteDataSource.getMovieReviews(MOVIE_ID, PAGE_NUMBER) }.throws(
            RuntimeException("Network error")
        )

        // When && Then
        val exception = assertThrows<RuntimeException> {
            repository.getMovieReviews(MOVIE_ID, PAGE_NUMBER)
        }
        assertThat(exception.message).contains("Network error")
    }

    @Test
    fun `getMovieReviews should return empty when remote has no results`() = runTest {
        //Given
        val fakeEmptyResponse = ApiResponse(
            currentPage = 1, items = emptyList<ReviewResponse>(), totalPages = 0, totalItems = 0
        )

        coEvery { movieRemoteDataSource.getMovieReviews(MOVIE_ID, PAGE_NUMBER) }.returns(
            Result.success(fakeEmptyResponse)
        )

        // When
        val result = repository.getMovieReviews(MOVIE_ID, PAGE_NUMBER)

        // Then
        assertThat(result.items).isEmpty()
    }

    @Test
    fun `getMovieAccountStatesById should use guestSessionId when userSessionId is null`() =
        runTest {
            val movieId = 123
            val guestSessionId = "guest123"
            val remoteMovieStates = fakeMovieStatesRemote()

            coEvery { authenticationPreferences.getSessionId() } returns null
            coEvery { authenticationPreferences.getGuestSessionId() } returns guestSessionId

            coEvery {
                movieRemoteDataSource.getAccountMovieStates(any(), any())
            } returns Result.success(remoteMovieStates)

            val result = repository.getAccountMovieStatesById(movieId)

            assertEquals(remoteMovieStates.toEntity(), result)
        }


    @Test
    fun `getTrendingMovies should return mapped trending movies`() = runTest {
        // Given
        val mockApiResponse = createMockTrendingMoviesApiResponse()
        coEvery { movieRemoteDataSource.getTrendingMovies(any()) } returns Result.success(
            mockApiResponse
        )

        // When
        val result = repository.getTrendingMovies(page = 1)

        // Then
        assertNotNull(result)
        Assert.assertEquals(1, result.currentPage)
        Assert.assertEquals(10, result.totalPages)
        Assert.assertEquals(100, result.totalItems)
        Assert.assertEquals(1, result.items.size)

        val trending = result.items.first()
        Assert.assertEquals(1, trending.id)
        Assert.assertEquals("Test Movie", trending.title)
        Assert.assertEquals("https://image.tmdb.org/t/p/w500test_poster.jpg", trending.posterPath)
    }


    @Test
    fun `getTrendingMovies should handle error from remote data source`() = runTest {
        // Given
        val error = Exception("Network error")
        coEvery { movieRemoteDataSource.getTrendingMovies(any()) } returns Result.failure(
            error
        )

        // When
        try {
            repository.getTrendingMovies(page = 1)
            assert(false)
        } catch (e: Exception) {
            // Then
            Assert.assertEquals("Network error", e.message)
        }
    }


    @Test
    fun `getTrendingMovies should handle pagination correctly`() = runTest {
        // Given
        val mockApiResponse = createMockTrendingMoviesApiResponse()
        coEvery { movieRemoteDataSource.getTrendingMovies(any()) } returns Result.success(
            mockApiResponse
        )

        // When
        val result1 = repository.getTrendingMovies(page = 1)
        val result2 = repository.getTrendingMovies(page = 2)

        // Then
        assertNotNull(result1)
        assertNotNull(result2)
        Assert.assertEquals(1, result1.currentPage)
        Assert.assertEquals(1, result2.currentPage)
    }


    @Test
    fun `getMoviesByCategory should return data from remote if available`() = runTest {
        //Given
        coEvery {
            movieRemoteDataSource.getMoviesByCategory(
                any(),
                PAGE_NUMBER
            )
        } returns Result.success(SearchMoviesRemoteMock)
        //When
        val result = repository.getMoviesByGenre(
            MovieGenre.TV_MOVIE,
            PAGE_NUMBER
        )
        //Then
        assertThat(result).isEqualTo(MovieList)
    }

    @Test
    fun `getMoviesByCategory should throw HttpLockedException when API returns 423`() = runTest {
        //Given
        coEvery {
            movieRemoteDataSource.getMoviesByCategory(
                any(),
                PAGE_NUMBER
            )
        } returns Result.failure(NetworkException.HttpLockedException(
            message = "Resource locked",
            status = 423
        ))
        //When //Then
        assertThrows<NetworkException.HttpLockedException> {
            repository.getMoviesByGenre(
                MovieGenre.TV_MOVIE,
                PAGE_NUMBER
            )
        }
    }

    @Test
    fun `addMovieRatingById returns true on success`() = runTest {
        // Given
        val movieId = 123
        val rating = 8
        val sessionId = "session123"
        val guestSessionId = "guest123"

        coEvery { authenticationPreferences.getSessionId() } returns sessionId
        coEvery { authenticationPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            movieRemoteDataSource.addMovieRating(
                movieId = movieId,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.success(createRatingResponse())

        // When
        val result = repository.addMovieRatingById(movieId, rating)

        // Then
        assertTrue(result)
    }

    @Test
    fun `addMovieRatingById returns false on failure`() = runTest {
        // Given
        val movieId = 123
        val rating = 8
        val sessionId = "session123"
        val guestSessionId = "guest123"

        coEvery { authenticationPreferences.getSessionId() } returns sessionId
        coEvery { authenticationPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            movieRemoteDataSource.addMovieRating(
                movieId = movieId,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.failure(RuntimeException("Network error"))

        // When
        val result = repository.addMovieRatingById(movieId, rating)

        // Then
        assertFalse(result)
    }


    @Test
    fun `addMovieRatingById handles null session id`() = runTest {
        // Given
        val movieId = 123
        val rating = 8
        val sessionId = null
        val guestSessionId = "guest123"

        coEvery { authenticationPreferences.getSessionId() } returns sessionId
        coEvery { authenticationPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            movieRemoteDataSource.addMovieRating(
                movieId = movieId,
                rating = rating.toDouble(),
                userSessionId = sessionId,
                guestSessionId = guestSessionId
            )
        } returns Result.success(createRatingResponse())

        // When
        val result = repository.addMovieRatingById(movieId, rating)

        // Then
        assertTrue(result)
    }

    @Test
    fun `getPopularMovies - when cache is empty should fetch from network and sync to cache`() =
        runTest {
            // Given
            val capturedItems = slot<List<PopularSectionLocal>>()
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { movieRemoteDataSource.getPopularMovies() } returns Result.success(
                singleMovieResponse
            )
            coEvery { homeLocalDataSource.insertAll(capture(capturedItems)) } returns Unit

            // When
            val result = repository.getPopularMovies()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo(101)
            assertThat(result[0].name).isEqualTo("Test Movie")

            coVerify(exactly = 1) { homeLocalDataSource.getAll() }
            coVerify(exactly = 1) { movieRemoteDataSource.getPopularMovies() }
            coVerify(exactly = 1) { homeLocalDataSource.insertAll(any()) }

            // Verify sync data
            assertThat(capturedItems.captured).hasSize(1)
            assertThat(capturedItems.captured[0].mediaType).isEqualTo(MediaType.Movie)
            assertThat(capturedItems.captured[0].id).isEqualTo(101)
        }

    @Test
    fun `getPopularMovies - when cache has data should return cached data without network call`() =
        runTest {
            // Given
            val cachedData = listOf(
                PopularSectionLocal(
                    id = 101,
                    name = "Cached Movie",
                    posterPictureUrl = "/cached_poster.jpg",
                    rating = 8.0,
                    mediaType = MediaType.Movie
                )
            )
            coEvery { homeLocalDataSource.getAll() } returns cachedData

            // When
            val result = repository.getPopularMovies()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo(101)
            assertThat(result[0].name).isEqualTo("Cached Movie")

            coVerify(exactly = 1) { homeLocalDataSource.getAll() }
            coVerify(exactly = 0) { movieRemoteDataSource.getPopularMovies() }
            coVerify(exactly = 0) { homeLocalDataSource.insertAll(any()) }
        }

    @Test
    fun `getPopularMovies - when cache has mixed media types should filter only movies`() =
        runTest {
            // Given
            val mixedCachedData = listOf(
                PopularSectionLocal(
                    id = 101,
                    name = "Movie",
                    posterPictureUrl = "/movie_poster.jpg",
                    rating = 8.0,
                    mediaType = MediaType.Movie
                ),
                PopularSectionLocal(
                    id = 201,
                    name = "TV Show",
                    posterPictureUrl = "/tv_poster.jpg",
                    rating = 9.0,
                    mediaType = MediaType.TvShow
                )
            )
            coEvery { homeLocalDataSource.getAll() } returns mixedCachedData

            // When
            val result = repository.getPopularMovies()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo(101)
            assertThat(result[0].name).isEqualTo("Movie")

            coVerify(exactly = 0) { movieRemoteDataSource.getPopularMovies() }
        }

    @Test
    fun `getPopularMovies - when network returns empty list should return empty list and sync empty data`() =
        runTest {
            // Given
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { movieRemoteDataSource.getPopularMovies() } returns Result.success(
                emptyMovieResponse
            )
            coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

            // When
            val result = repository.getPopularMovies()

            // Then
            assertThat(result).isEmpty()
            coVerify(exactly = 1) { homeLocalDataSource.insertAll(emptyList()) }
        }

    @Test
    fun `getPopularMovies - when network returns multiple items should map and sync all items`() =
        runTest {
            // Given
            val capturedItems = slot<List<PopularSectionLocal>>()
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { movieRemoteDataSource.getPopularMovies() } returns Result.success(
                multipleMoviesResponse
            )
            coEvery { homeLocalDataSource.insertAll(capture(capturedItems)) } returns Unit

            // When
            val result = repository.getPopularMovies()

            // Then
            assertThat(result).hasSize(2)
            assertThat(result[0].name).isEqualTo("Test Movie 1")
            assertThat(result[1].name).isEqualTo("Test Movie 2")

            // Verify all items were synced
            assertThat(capturedItems.captured).hasSize(2)
            assertThat(capturedItems.captured.all { it.mediaType == MediaType.Movie }).isTrue()
        }

    @Test
    fun `getPopularMovies - when network throws UnAuthorizedException should propagate exception and log crash`() =
        runTest {
            // Given
            val exception = NetworkException.UnAuthorizedException(
                message = "401 Unauthorized",
                status = 401
            )
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { movieRemoteDataSource.getPopularMovies() } throws exception

            // When & Then
            assertThrows<NetworkException.UnAuthorizedException> {
                repository.getPopularMovies()
            }

            verify { crashReporter.logException(exception) }
            coVerify(exactly = 0) { homeLocalDataSource.insertAll(any()) }
        }

    @Test
    fun `getPopularMovies - when network throws TimeoutException should propagate exception and log crash`() =
        runTest {
            // Given
            val exception = NetworkException.TimeoutException(
                message = "Request timed out",
                status = 408
            )
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { movieRemoteDataSource.getPopularMovies() } throws exception

            // When & Then
            assertThrows<NetworkException.TimeoutException> {
                repository.getPopularMovies()
            }

            verify { crashReporter.logException(exception) }
        }

    @Test
    fun `getPopularMovies - when network returns failure result should throw exception`() =
        runTest {
            // Given
            val exception = RuntimeException("Network error")
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { movieRemoteDataSource.getPopularMovies() } returns Result.failure(exception)

            // When & Then
            assertThrows<RuntimeException> {
                repository.getPopularMovies()
            }
        }


    @Test
    fun `getPopularMovies - should correctly map all movie fields from network response`() =
        runTest {
            // Given
            coEvery { homeLocalDataSource.getAll() } returns emptyList()
            coEvery { movieRemoteDataSource.getPopularMovies() } returns Result.success(
                singleMovieResponse
            )
            coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

            // When
            val result = repository.getPopularMovies()

            // Then
            val movie = result[0]
            assertThat(movie.id).isEqualTo(101)
            assertThat(movie.name).isEqualTo("Test Movie")
            assertThat(movie.posterUrl).contains("/poster.jpg")
            assertThat(movie.rating).isEqualTo(7.8)
        }


    @Test
    fun `getPopularMovies - when cache has only tv shows should fetch from network`() = runTest {
        // Given
        val tvShowCacheData = listOf(
            PopularSectionLocal(
                id = 201,
                name = "TV Show Only",
                posterPictureUrl = "/tv_poster.jpg",
                rating = 9.0,
                mediaType = MediaType.TvShow
            )
        )
        coEvery { homeLocalDataSource.getAll() } returns tvShowCacheData
        coEvery { movieRemoteDataSource.getPopularMovies() } returns Result.success(
            singleMovieResponse
        )
        coEvery { homeLocalDataSource.insertAll(any()) } returns Unit

        // When
        val result = repository.getPopularMovies()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("Test Movie")

        coVerify(exactly = 1) { movieRemoteDataSource.getPopularMovies() }
    }

    private companion object {
        const val CATEGORY_ID = 2
        const val MOVIE_ID = 1
        const val PAGE_NUMBER = 1

        val MovieList = PagedFetchResponse(
            PAGE_NUMBER,
            listOf(
                Movie(
                    id = 1,
                    name = "",
                    posterUrl = "https://image.tmdb.org/t/p/w500",
                    releaseYear = 2020,
                    rating = 8,
                    genres = listOf(),
                )
            ),
            totalItems = 1,
            totalPages = 1
        )
        val TvShowList = PagedFetchResponse(
            PAGE_NUMBER,
            listOf(
                TvShow(
                    id = 2,
                    name = "",
                    posterPicture = "https://image.tmdb.org/t/p/w500",
                    releaseYear = 2020,
                    rating = 10,
                    genres = listOf(),
                )
            ),
            totalItems = 1,
            totalPages = 1
        )
        private val SearchMoviesRemoteMock = ApiResponse(
            currentPage = PAGE_NUMBER,
            items = listOf(
                MovieRemote(
                    genreIds = emptyList(),
                    id = 1,
                    posterPath = "",
                    releaseDate = "2020-06-15",
                    voteAverage = 8.0,
                    name = "",
                )
            ),
            totalPages = 1,
            totalItems = 1
        )

        private val SearchTvShowRemoteMock = ApiResponse(
            currentPage = PAGE_NUMBER,
            items = listOf(
                SearchTvShowRemote(
                    genreIds = emptyList(),
                    id = 2,
                    posterPath = "",
                    firstAirDate = "2020-07-20",
                    name = "",
                    voteAverage = 10.0,
                )
            ),
            totalPages = 1,
            totalItems = 1
        )

    }


    private fun fakeMovieDetailsRemote() = MovieDetailsResponse(
        backdropPath = "/b.jpg",
        genreRemote = listOf(
            GenreRemote(1),
            GenreRemote(2)
        ),
        id = 123,
        overview = "dream",
        posterPath = "/p.jpg",
        releaseDate = "2010-07-16",
        runtime = 148,
        title = "Inception",
        video = false,
        voteAverage = 8.8,
    )

    private fun fakeSimilarMoviesRemote() = ApiResponse(
        currentPage = 1,
        items = listOf(
            MovieRemote(
                genreIds = listOf(1, 2, 3),
                id = 1,
                posterPath = "",
                releaseDate = "2020-06-15",
                voteAverage = 8.0,
                name = "",
            ),
            MovieRemote(
                genreIds = listOf(1, 2, 3),
                id = 1,
                posterPath = "",
                releaseDate = "2020-06-15",
                voteAverage = 8.0,
                name = "",
            )
        ),
        totalPages = 1,
        totalItems = 1
    )

    private fun fakeMovieImagesRemote() = MovieImagesResponse(
        backdrops = emptyList(),
        id = 123,
        logos = emptyList(),
        posters = listOf(
            ImageItem(
                filePath = "/img1.jpg",
            ),
            ImageItem(
                filePath = "/img2.jpg",
            )
        )
    )

    private fun fakeMovieStatesRemote(): AccountStatesResponse =
        AccountStatesResponse(
            id = 123,
            favorite = true,
            watchlist = true
        )

    private fun secondFakeMovieStatesRemote(): AccountStatesResponse {
        return AccountStatesResponse(
            favorite = true,
            id = 5,
            watchlist = false
        )
    }

    private fun createMockTrendingMoviesApiResponse(): ApiResponse<TrendingResponse> {
        val mockTrendingItem = TrendingResponse(
            id = 1,
            title = "Test Movie",
            posterPath = "test_poster.jpg",
            genreIds = listOf(28, 12)
        )

        return ApiResponse(
            currentPage = 1,
            items = listOf(mockTrendingItem),
            totalPages = 10,
            totalItems = 100
        )

    }

    val singleMovieResponse = ApiResponse(
        currentPage = 1,
        totalItems = 100,
        totalPages = 200,
        items = listOf(
            PopularMovieResponse(
                id = 101,
                posterPath = "/poster.jpg",
                title = "Test Movie",
                voteAverage = 7.8,
            )
        )
    )

    val emptyMovieResponse = ApiResponse<PopularMovieResponse>(
        currentPage = 1,
        totalItems = 0,
        totalPages = 0,
        items = emptyList()
    )

    val multipleMoviesResponse = ApiResponse(
        currentPage = 1,
        totalItems = 2,
        totalPages = 1,
        items = listOf(
            PopularMovieResponse(
                id = 101,
                posterPath = "/poster1.jpg",
                title = "Test Movie 1",
                voteAverage = 7.8,
            ),
            PopularMovieResponse(
                id = 102,
                posterPath = "/poster2.jpg",
                title = "Test Movie 2",
                voteAverage = 8.2,
            )
        )
    )

    private fun createRatingResponse() = RatingRemoteResponse(
        statusCode = 1,
        statusMessage = "Success"
    )
}