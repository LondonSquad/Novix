package com.london.data.repository

import com.google.common.truth.Truth.assertThat
import com.london.data.datasource.exception.NetworkException
import com.london.data.datasource.remote.details.actordetails.ActorDetailsRemoteDataSource
import com.london.data.datasource.remote.details.actordetails.model.ActorDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actorimage.ActorImageResponse
import com.london.data.datasource.remote.details.actordetails.model.actormoviedetails.ActorMovieDetailsResponse
import com.london.data.datasource.remote.details.actordetails.model.actortvshowdetails.ActorTvShowDetailsResponse
import com.london.data.mapper.actordetails.toEntity
import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class ActorRepositoryImplTest {

    private lateinit var remoteDataSource: ActorDetailsRemoteDataSource
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
    fun `getActorMoviePicksById returns expected result`() = runTest {
        coEvery { remoteDataSource.getActorMovieById(ACTOR_ID) } returns Result.success(
            ActorMovieDetailsRemoteMock
        )

        val result = repository.getActorMoviePicksById(ACTOR_ID)

        assertThat(result).isEqualTo(ActorMovieDetailsRemoteMock.toEntity())
    }


    @Test
    fun `getActorTvShowPicksById returns expected result`() = runTest {
        coEvery { remoteDataSource.getActorTvShowById(ACTOR_ID) } returns Result.success(
            ActorTvShowDetailsRemoteMock
        )

        val result = repository.getActorTvShowPicksById(ACTOR_ID)

        assertThat(result).isEqualTo(ActorTvShowDetailsRemoteMock.toEntity())
    }


    @Test
    fun `getActorImagesById returns expected result`() = runTest {
        coEvery { remoteDataSource.getActorImagePath(ACTOR_ID) } returns Result.success(
            ActorImageResponseMock
        )

        val result = repository.getActorImagesById(ACTOR_ID)

        assertThat(result).isEqualTo(ActorImageResponseMock.toEntity())
    }

    @Test
    fun `getActorTvShowPicksById throws UnAuthorizedException on failure`() = runTest {
        coEvery { remoteDataSource.getActorTvShowById(ACTOR_ID) } returns Result.failure(
            NetworkException.UnAuthorizedException("unauthorized")
        )

        assertThrows<NetworkException.UnAuthorizedException> {
            repository.getActorTvShowPicksById(ACTOR_ID)
        }
    }

    @Test
    fun `getActorDetailsById throws HttpLockedException on failure`() = runTest {
        coEvery { remoteDataSource.getActorDetailsById(ACTOR_ID) } returns Result.failure(
            NetworkException.HttpLockedException("locked")
        )

        assertThrows<NetworkException.HttpLockedException> {
            repository.getActorDetailsById(ACTOR_ID)
        }
    }

    @Test
    fun `getActorImagePath throws ValidationException on failure`() = runTest {
        coEvery { remoteDataSource.getActorImagePath(ACTOR_ID) } returns Result.failure(
            NetworkException.ValidationException("validation failed")
        )

        assertThrows<NetworkException.ValidationException> {
            repository.getActorImagesById(ACTOR_ID)
        }
    }

    @Test
    fun `getActorTvShowPicksById throws TimeoutException on failure`() = runTest {
        coEvery { remoteDataSource.getActorMovieById(ACTOR_ID) } returns Result.failure(
            NetworkException.TimeoutException("timeout")
        )

        assertThrows<NetworkException.TimeoutException> {
            repository.getActorMoviePicksById(ACTOR_ID)
        }
    }


    private companion object {
        const val ACTOR_ID = 123

        val ActorDetailsRemoteMock = ActorDetailsResponse(
            id = ACTOR_ID,
            name = "John Doe",
            gender = 2,
            adult = false,
            birthday = "1980-01-01",
            deathDay = null,
            placeOfBirth = "London",
            biography = "An amazing actor",
            alsoKnownAs = listOf("JD", "Johnny D"),
            homePage = "https://john-doe.com",
            imdbId = "nm1234567",
            knownForDepartment = "Acting",
            popularity = 99.9,
            profilePath = "/profile.jpg"
        )

        val ActorMovieDetailsRemoteMock = ActorMovieDetailsResponse(
            id = ACTOR_ID,
            cast = emptyList(),
            crew = emptyList()
        )

        val ActorTvShowDetailsRemoteMock = ActorTvShowDetailsResponse(
            id = ACTOR_ID,
            cast = emptyList(),
            crew = emptyList()
        )

        val ActorImageResponseMock = ActorImageResponse(
            id = ACTOR_ID,
            profiles = emptyList()
        )
    }
}
