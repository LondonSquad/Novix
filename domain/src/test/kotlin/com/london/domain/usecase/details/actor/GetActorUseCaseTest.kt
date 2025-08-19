package com.london.domain.usecase.details.actor

import com.london.domain.entity.actor.Actor
import com.london.domain.entity.actor.ActorDetails
import com.london.domain.entity.actor.ActorImageDetails
import com.london.domain.entity.actor.ActorMediaDetails
import com.london.domain.entity.shared.PagedFetchResponse
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.MovieRepository
import com.london.domain.repository.TvShowRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GetActorUseCaseTest {
    private lateinit var actorRepository: ActorRepository
    private lateinit var movieRepository: MovieRepository
    private lateinit var tvShowRepository: TvShowRepository
    private lateinit var getActorUseCase: GetActorUseCase

    @Before
    fun setup() {
        actorRepository = mockk()
        movieRepository = mockk()
        tvShowRepository = mockk()
        getActorUseCase = GetActorUseCase(
            actorRepository = actorRepository,
            movieRepository = movieRepository,
            tvShowRepository = tvShowRepository
        )
    }

    @Test
    fun `should call repository getActorDetailsById with correct id and return result`() = runTest {
        // Given
        val actorId = 1
        val expectedResult = mockk<ActorDetails>()
        coEvery { actorRepository.getActorDetailsById(actorId) } returns expectedResult

        // When
        val result = getActorUseCase.getActorDetailsById(actorId)

        // Then
        coVerify(exactly = 1) { actorRepository.getActorDetailsById(actorId) }
        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun `should call repository getActorImagesById with correct id and return profiles list`() =
        runTest {
            // Given
            val actorId = 1
            val mockImageDetails = listOf(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg"
            )
            val mockActorImageDetails = mockk<ActorImageDetails> {
                coEvery { imageUrl } returns mockImageDetails
            }
            coEvery { actorRepository.getActorImagesById(actorId) } returns mockActorImageDetails

            // When
            val result = getActorUseCase.getActorImagesById(actorId)

            // Then
            coVerify(exactly = 1) { actorRepository.getActorImagesById(actorId) }
            Assert.assertEquals(mockImageDetails, result)
        }

    @Test
    fun `should return empty list when profiles is empty`() = runTest {
        // Given
        val actorId = 1
        val emptyImageDetails = emptyList<String>()
        val mockActorImageDetails = mockk<ActorImageDetails> {
            coEvery { imageUrl } returns emptyImageDetails
        }
        coEvery { actorRepository.getActorImagesById(actorId) } returns mockActorImageDetails

        // When
        val result = getActorUseCase.getActorImagesById(actorId)

        // Then
        coVerify(exactly = 1) { actorRepository.getActorImagesById(actorId) }
        Assert.assertEquals(emptyImageDetails, result)
        Assert.assertEquals(0, result.size)
    }

    @Test
    fun `should handle different actor ids correctly`() = runTest {
        // Given
        val actorId = 999
        val mockImageDetails = listOf(
            "https://test.com/image1.jpg",
            "https://test.com/image2.jpg"
        )
        val mockActorImageDetails = mockk<ActorImageDetails> {
            coEvery { imageUrl } returns mockImageDetails
        }
        coEvery { actorRepository.getActorImagesById(actorId) } returns mockActorImageDetails

        // When
        val result = getActorUseCase.getActorImagesById(actorId)

        // Then
        coVerify(exactly = 1) { actorRepository.getActorImagesById(actorId) }
        Assert.assertEquals(mockImageDetails, result)
    }

    @Test
    fun `should call repository getActorMoviePicksById with correct id and return result`() =
        runTest {
            // Given
            val actorId = 1
            val expectedResult = mockk<ActorMediaDetails>()
            coEvery { movieRepository.getActorMoviePicksById(actorId) } returns expectedResult

            // When
            val result = getActorUseCase.getActorMoviePicksById(actorId)

            // Then
            coVerify(exactly = 1) { movieRepository.getActorMoviePicksById(actorId) }
            assertEquals(expectedResult, result)
        }

    @Test
    fun `should call repository getActorTvShowPicksById with correct id and return result`() =
        runTest {
            // Given
            val actorId = 1
            val expectedResult = mockk<ActorMediaDetails>()
            coEvery { tvShowRepository.getActorTvShowPicksById(actorId) } returns expectedResult

            // When
            val result = getActorUseCase.getActorTvShowPicksById(actorId)

            // Then
            coVerify(exactly = 1) { tvShowRepository.getActorTvShowPicksById(actorId) }
            assertEquals(expectedResult, result)
        }


    @Test
    fun `invoke should return trending actors from repository`() = runTest {
        val mockResponse = createMockActorsResponse()
        coEvery { actorRepository.getTrendingActors(any()) } returns mockResponse

        val result = getActorUseCase.getTrendingActors(page = 1)

        assertNotNull(result)
        Assert.assertEquals(1, result.currentPage)
        Assert.assertEquals(10, result.totalPages)
        Assert.assertEquals(100, result.totalItems)
        Assert.assertEquals(1, result.items.size)

        val actor = result.items.first()
        Assert.assertEquals(1, actor.id)
        Assert.assertEquals("Test Actor", actor.name)
        Assert.assertEquals("test_profile.jpg", actor.profilePictureUrl)
        Assert.assertEquals("", actor.characterName)
    }

    @Test
    fun `invoke should handle different page numbers`() = runTest {
        val mockResponse = createMockActorsResponse()
        coEvery { actorRepository.getTrendingActors(any()) } returns mockResponse

        val result1 = getActorUseCase.getTrendingActors(page = 1)
        val result2 = getActorUseCase.getTrendingActors(page = 2)

        assertNotNull(result1)
        assertNotNull(result2)
        Assert.assertEquals(1, result1.currentPage)
        Assert.assertEquals(1, result2.currentPage)
    }

    @Test
    fun `invoke should handle empty response`() = runTest {
        val emptyResponse = PagedFetchResponse<Actor>(
            currentPage = 1,
            items = emptyList(),
            totalPages = 0,
            totalItems = 0
        )
        coEvery { actorRepository.getTrendingActors(any()) } returns emptyResponse

        val result = getActorUseCase.getTrendingActors(page = 1)

        assertNotNull(result)
        Assert.assertEquals(1, result.currentPage)
        Assert.assertEquals(0, result.totalPages)
        Assert.assertEquals(0, result.totalItems)
        Assert.assertEquals(0, result.items.size)
    }

    @Test
    fun `invoke should handle multiple actors in response`() = runTest {
        val multipleActorsResponse = PagedFetchResponse<Actor>(
            currentPage = 1,
            items = listOf(
                createMockActor(id = 1, name = "Actor 1"),
                createMockActor(id = 2, name = "Actor 2"),
                createMockActor(id = 3, name = "Actor 3")
            ),
            totalPages = 10,
            totalItems = 100
        )
        coEvery { actorRepository.getTrendingActors(any()) } returns multipleActorsResponse

        val result = getActorUseCase.getTrendingActors(page = 1)

        assertNotNull(result)
        Assert.assertEquals(3, result.items.size)
        Assert.assertEquals("Actor 1", result.items[0].name)
        Assert.assertEquals("Actor 2", result.items[1].name)
        Assert.assertEquals("Actor 3", result.items[2].name)
    }

    @Test
    fun `invoke should handle repository error`() = runTest {
        val error = Exception("Repository error")
        coEvery { actorRepository.getTrendingActors(any()) } throws error

        try {
            getActorUseCase.getTrendingActors(page = 1)
            assert(false)
        } catch (e: Exception) {
            Assert.assertEquals("Repository error", e.message)
        }
    }

    @Test
    fun `invoke should handle negative page number`() = runTest {
        val mockResponse = createMockActorsResponse()
        coEvery { actorRepository.getTrendingActors(any()) } returns mockResponse

        val result = getActorUseCase.getTrendingActors(page = -1)

        assertNotNull(result)
        Assert.assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle zero page number`() = runTest {
        val mockResponse = createMockActorsResponse()
        coEvery { actorRepository.getTrendingActors(any()) } returns mockResponse

        val result = getActorUseCase.getTrendingActors(page = 0)

        assertNotNull(result)
        Assert.assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle large page number`() = runTest {
        val mockResponse = createMockActorsResponse()
        coEvery { actorRepository.getTrendingActors(any()) } returns mockResponse

        val result = getActorUseCase.getTrendingActors(page = 999)

        assertNotNull(result)
        Assert.assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle actors with different character names`() = runTest {
        val actorsWithDifferentCharacterNames = PagedFetchResponse<Actor>(
            currentPage = 1,
            items = listOf(
                createMockActor(id = 1, name = "Actor 1", characterName = "Character 1"),
                createMockActor(id = 2, name = "Actor 2", characterName = "Character 2"),
                createMockActor(id = 3, name = "Actor 3", characterName = "")
            ),
            totalPages = 10,
            totalItems = 100
        )
        coEvery { actorRepository.getTrendingActors(any()) } returns actorsWithDifferentCharacterNames

        val result = getActorUseCase.getTrendingActors(page = 1)

        assertNotNull(result)
        Assert.assertEquals(3, result.items.size)
        Assert.assertEquals("Character 1", result.items[0].characterName)
        Assert.assertEquals("Character 2", result.items[1].characterName)
        Assert.assertEquals("", result.items[2].characterName)
    }

    @Test
    fun `invoke should handle actors with different profile pictures`() = runTest {
        val actorsWithDifferentProfilePictures = PagedFetchResponse(
            currentPage = 1,
            items = listOf(
                createMockActor(id = 1, name = "Actor 1", profilePicture = "profile1.jpg"),
                createMockActor(id = 2, name = "Actor 2", profilePicture = "profile2.jpg"),
                createMockActor(id = 3, name = "Actor 3", profilePicture = "")
            ),
            totalPages = 10,
            totalItems = 100
        )
        coEvery { actorRepository.getTrendingActors(any()) } returns actorsWithDifferentProfilePictures

        val result = getActorUseCase.getTrendingActors(page = 1)

        assertNotNull(result)
        Assert.assertEquals(3, result.items.size)
        Assert.assertEquals("profile1.jpg", result.items[0].profilePictureUrl)
        Assert.assertEquals("profile2.jpg", result.items[1].profilePictureUrl)
        Assert.assertEquals("", result.items[2].profilePictureUrl)
    }

    private fun createMockActorsResponse(): PagedFetchResponse<Actor> =
        PagedFetchResponse(
            currentPage = 1,
            items = listOf(createMockActor()),
            totalPages = 10,
            totalItems = 100
        )

    private fun createMockActor(
        id: Int = 1,
        name: String = "Test Actor",
        profilePicture: String = "test_profile.jpg",
        characterName: String = ""
    ): Actor {
        return Actor(
            id = id,
            name = name,
            profilePictureUrl = profilePicture,
            characterName = characterName
        )
    }
}
