package com.london.domain.usecase

import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.TrendingRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class GetTrendingActorsUseCaseTest {

    private lateinit var useCase: GetTrendingActorsUseCase
    private lateinit var repository: TrendingRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetTrendingActorsUseCase(repository)
    }

    @Test
    fun `invoke should return trending actors from repository`() = runTest {
        val mockResponse = createMockActorsResponse()
        coEvery { repository.getTrendingActors(any()) } returns mockResponse

        val result = useCase.invoke(page = 1)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
        assertEquals(10, result.totalPages)
        assertEquals(100, result.totalItems)
        assertEquals(1, result.items.size)

        val actor = result.items.first()
        assertEquals(1, actor.id)
        assertEquals("Test Actor", actor.name)
        assertEquals("test_profile.jpg", actor.profilePicture)
        assertEquals("", actor.characterName)
    }

    @Test
    fun `invoke should handle different page numbers`() = runTest {
        val mockResponse = createMockActorsResponse()
        coEvery { repository.getTrendingActors(any()) } returns mockResponse

        val result1 = useCase.invoke(page = 1)
        val result2 = useCase.invoke(page = 2)

        assertNotNull(result1)
        assertNotNull(result2)
        assertEquals(1, result1.currentPage)
        assertEquals(1, result2.currentPage)
    }

    @Test
    fun `invoke should handle empty response`() = runTest {
        val emptyResponse = PagedFetchResponse<Actor>(
            currentPage = 1,
            items = emptyList(),
            totalPages = 0,
            totalItems = 0
        )
        coEvery { repository.getTrendingActors(any()) } returns emptyResponse

        val result = useCase.invoke(page = 1)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
        assertEquals(0, result.totalPages)
        assertEquals(0, result.totalItems)
        assertEquals(0, result.items.size)
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
        coEvery { repository.getTrendingActors(any()) } returns multipleActorsResponse

        val result = useCase.invoke(page = 1)

        assertNotNull(result)
        assertEquals(3, result.items.size)
        assertEquals("Actor 1", result.items[0].name)
        assertEquals("Actor 2", result.items[1].name)
        assertEquals("Actor 3", result.items[2].name)
    }

    @Test
    fun `invoke should handle repository error`() = runTest {
        val error = Exception("Repository error")
        coEvery { repository.getTrendingActors(any()) } throws error

        try {
            useCase.invoke(page = 1)
            assert(false)
        } catch (e: Exception) {
            assertEquals("Repository error", e.message)
        }
    }

    @Test
    fun `invoke should handle negative page number`() = runTest {
        val mockResponse = createMockActorsResponse()
        coEvery { repository.getTrendingActors(any()) } returns mockResponse

        val result = useCase.invoke(page = -1)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle zero page number`() = runTest {
        val mockResponse = createMockActorsResponse()
        coEvery { repository.getTrendingActors(any()) } returns mockResponse

        val result = useCase.invoke(page = 0)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle large page number`() = runTest {
        val mockResponse = createMockActorsResponse()
        coEvery { repository.getTrendingActors(any()) } returns mockResponse

        val result = useCase.invoke(page = 999)

        assertNotNull(result)
        assertEquals(1, result.currentPage)
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
        coEvery { repository.getTrendingActors(any()) } returns actorsWithDifferentCharacterNames

        val result = useCase.invoke(page = 1)

        assertNotNull(result)
        assertEquals(3, result.items.size)
        assertEquals("Character 1", result.items[0].characterName)
        assertEquals("Character 2", result.items[1].characterName)
        assertEquals("", result.items[2].characterName)
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
        coEvery { repository.getTrendingActors(any()) } returns actorsWithDifferentProfilePictures

        val result = useCase.invoke(page = 1)

        assertNotNull(result)
        assertEquals(3, result.items.size)
        assertEquals("profile1.jpg", result.items[0].profilePicture)
        assertEquals("profile2.jpg", result.items[1].profilePicture)
        assertEquals("", result.items[2].profilePicture)
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
            profilePicture = profilePicture,
            characterName = characterName
        )
    }
} 