package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.mapper.details.actor.toEntity
import com.london.data.mapper.details.tvshow.toCastEntity
import com.london.data.remote.exception.NetworkException
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.actor.model.ActorDetailsResponse
import com.london.data.remote.model.details.actor.model.actorimage.ActorImageResponse
import com.london.data.remote.model.details.movie.model.moviecast.MovieActor
import com.london.data.remote.model.details.movie.model.moviecast.MovieCastResponse
import com.london.data.remote.model.details.tvshow.model.Role
import com.london.data.remote.model.details.tvshow.model.TvShowCastMember
import com.london.data.remote.model.details.tvshow.model.TvShowCastRemoteResponse
import com.london.data.remote.model.home.trending.TrendingResponse
import com.london.data.remote.source.actor.ActorRemoteDataSource
import com.london.data.repository.actor.ActorRepositoryImpl
import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows

class ActorRepositoryImplTest {

    private lateinit var remoteDataSource: ActorRemoteDataSource
    private lateinit var repository: ActorRepository

    @Before
    fun setUp() {
        remoteDataSource = mockk(relaxed = true)
        repository = ActorRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getActorDetailsById returns expected result`() = runTest {
        coEvery { remoteDataSource.getActorDetailsById(ACTOR_ID) } returns Result.success(
            ActorDetailsRemoteMock
        )

        val result = repository.getActorDetailsById(ACTOR_ID)

        assertThat(result).isEqualTo(ActorDetailsRemoteMock.toEntity())
    }

    @Test
    fun `getActorImagesById returns expected result`() = runTest {
        coEvery { remoteDataSource.getActorImagePathById(ACTOR_ID) } returns Result.success(
            ActorImageResponseMock
        )

        val result = repository.getActorImagesById(ACTOR_ID)

        assertThat(result).isEqualTo(ActorImageResponseMock.toEntity())
    }

    @Test
    fun `getMovieCastById return expected result`() = runTest {
        coEvery { remoteDataSource.getMovieActors(ACTOR_ID) } returns Result.failure(
            NetworkException.UnAuthorizedException(
                message = "unauthorized",
                status = 401
            )
        )

        assertThrows<NetworkException.UnAuthorizedException> {
            repository.getMovieActors(ACTOR_ID)
        }
    }

    @Test
    fun `getCastTvShowById return expected result`() = runTest {
        val tvShowCastRemoteResponse = mockk<TvShowCastRemoteResponse>(relaxed = true)
        val expectedEntity = tvShowCastRemoteResponse.toCastEntity()

        coEvery { remoteDataSource.getTvShowActors(ACTOR_ID) } returns Result.success(
            tvShowCastRemoteResponse
        )

        val result = repository.getTvShowActors(ACTOR_ID)

        assertThat(result).isEqualTo(expectedEntity)
    }

    @Test
    fun `getActorDetailsById throws HttpLockedException on failure`() = runTest {
        coEvery { remoteDataSource.getActorDetailsById(ACTOR_ID) } returns Result.failure(
            NetworkException.HttpLockedException(
                message = "locked",
                status = 423
            )
        )

        assertThrows<NetworkException.HttpLockedException> {
            repository.getActorDetailsById(ACTOR_ID)
        }
    }

    @Test
    fun `getActorImagePath throws ValidationException on failure`() = runTest {
        coEvery { remoteDataSource.getActorImagePathById(ACTOR_ID) } returns Result.failure(
            NetworkException.ValidationException(
                message = "validation failed",
                status = 400
            )
        )

        assertThrows<NetworkException.ValidationException> {
            repository.getActorImagesById(ACTOR_ID)
        }
    }

    @Test
    fun `getTrendingActors should return expected result`() = runTest {
        coEvery { remoteDataSource.getTrendingActors(ACTOR_ID) } returns Result.failure(
            NetworkException.TimeoutException(
                message = "timeout",
                status = 408
            )
        )

        assertThrows<NetworkException.TimeoutException> {
            repository.getTrendingActors(ACTOR_ID)
        }
    }

    @Test
    fun `getMovieCast should return actor list with names and characters`() = runTest {
        coEvery { remoteDataSource.getMovieActors(123) } returns Result.success(
            fakeMovieCastRemote
        )

        val result = repository.getMovieActors(123)

        assertEquals(2, result.size)
        assertEquals("Leonardo DiCaprio", result[0].name)
        assertEquals("Cobb", result[0].characterName)
        assertEquals("Arthur", result[1].characterName)
    }


    @Test
    fun `getMovieCast should throw ValidationException when remote fails`() = runTest {
        coEvery { remoteDataSource.getMovieActors(123) } throws
                NetworkException.ValidationException(
                    message = "validation error",
                    status = 400
                )

        assertThrows<NetworkException.ValidationException> {
            repository.getMovieActors(123)
        }
    }

    @Test
    fun `getTrendingActors should handle error from remote data source`() = runTest {
        // Given
        val error = Exception("Network error")
        coEvery { remoteDataSource.getTrendingActors(any()) } returns Result.failure(error)

        // When
        try {
            repository.getTrendingActors(page = 1)
            assert(false)
        } catch (e: Exception) {
            // Then
            Assert.assertEquals("Network error", e.message)
        }
    }


    @Test
    fun `getTrendingActors should return mapped trending actors`() = runTest {
        // Given
        val mockApiResponse = createMockTrendingActorsApiResponse()
        coEvery { remoteDataSource.getTrendingActors(any()) } returns Result.success(mockApiResponse)

        // When
        val result = repository.getTrendingActors(page = 1)

        // Then
        assertEquals(1, result.currentPage)
        assertEquals(10, result.totalPages)
        assertEquals(100, result.totalItems)
        assertEquals(1, result.items.size)

        val actor = result.items.first()
        assertEquals(1, actor.id)
        assertEquals("Test Actor", actor.name)
        assertEquals("https://image.tmdb.org/t/p/w500test_profile.jpg", actor.profilePictureUrl)
        assertEquals("", actor.characterName)
    }

    @Test
    fun `getTrendingActors should handle pagination correctly`() = runTest {
        // Given
        val mockApiResponse = createMockTrendingActorsApiResponse()
        coEvery { remoteDataSource.getTrendingActors(any()) } returns Result.success(mockApiResponse)

        // When
        val result1 = repository.getTrendingActors(page = 1)
        val result2 = repository.getTrendingActors(page = 2)

        // Then
        assertNotNull(result1)
        assertNotNull(result2)
        Assert.assertEquals(1, result1.currentPage)
        Assert.assertEquals(1, result2.currentPage)
    }


    @Test
    fun `getCastTvShowById should return TvShowCastEntity when remote call succeeds`() = runTest {
        coEvery { remoteDataSource.getTvShowActors(TV_SHOW_ID) }.returns(
            Result.success(
                tvShowCastRemoteMock
            )
        )

        val result = repository.getTvShowActors(TV_SHOW_ID)

        assertThat(result).isEqualTo(tvShowCastRemoteMock.toCastEntity())
    }

    @Test
    fun `getCastsByTvShowId should throw UnAuthorizedException when remote fails`() = runTest {
        coEvery {
            remoteDataSource.getTvShowActors(123)
        } throws NetworkException.UnAuthorizedException(
            message = "unAuthorized error",
            status = 401
        )

        assertThrows<NetworkException.UnAuthorizedException> {
            repository.getTvShowActors(123)
        }
    }

    private val tvShowCastRemoteMock = TvShowCastRemoteResponse(
        cast = listOf(
            TvShowCastMember(
                id = 1,
                name = "Actor Name",
                profilePath = "/actor.jpg",
                roles = listOf(
                    Role(
                       character = "Main Character", episodeCount = 10
                    )
                ),
            )
        ), id = TV_SHOW_ID
    )

    private companion object {
        const val ACTOR_ID = 123
        private const val TV_SHOW_ID = 1

        val ActorDetailsRemoteMock = ActorDetailsResponse(
            id = ACTOR_ID,
            name = "John Doe",
            birthday = "1980-01-01",
            deathDay = null,
            placeOfBirth = "London",
            biography = "An amazing actor",
            knownForDepartment = "Acting",
            profilePath = "/profile.jpg"
        )

        val ActorImageResponseMock = ActorImageResponse(
            id = ACTOR_ID,
            profiles = emptyList()
        )

        val fakeMovieCastRemote = MovieCastResponse(
            id = 123,
            actorRemote = listOf(
                MovieActor(
                    id = 1,
                    originalName = "Leonardo DiCaprio",
                    profilePath = "/leo.jpg",
                    character = "Cobb",
                ),
                MovieActor(
                    id = 2,
                    originalName = "Joseph Gordon-Levitt",
                    profilePath = "/jgl.jpg",
                    character = "Arthur",
                )
            ),
        )
    }

    private fun createMockTrendingActorsApiResponse(): ApiResponse<TrendingResponse> {
        val mockTrendingItem = TrendingResponse(
            id = 1,
            name = "Test Actor",
            profilePath = "test_profile.jpg"
        )

        return ApiResponse(
            currentPage = 1,
            items = listOf(mockTrendingItem),
            totalPages = 10,
            totalItems = 100
        )
    }
}