package com.london.domain.usecase

import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class GetTrendingActorsUseCaseTest {

    private lateinit var useCase: GetTrendingActorsUseCase
    private lateinit var repository: ActorRepository

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = GetTrendingActorsUseCase(repository)
    }

    @Test
    fun `invoke should return trending actors from repository`() = runTest {
        val mockResponse = fakePagedActors()
        coEvery { repository.getTrendingActors(any()) } returns mockResponse

        val result = useCase.invoke(page = 1)

        assertEquals(mockResponse, result)
        assertEquals(mockResponse.items.first(), result.items.first())
    }

    @Test
    fun `invoke should handle different page numbers`() = runTest {
        val mockResponse = fakePagedActors()
        coEvery { repository.getTrendingActors(any()) } returns mockResponse

        val result1 = useCase.invoke(page = 1)
        val result2 = useCase.invoke(page = 2)

        assertEquals(1, result1.currentPage)
        assertEquals(1, result2.currentPage)
    }

    @Test
    fun `invoke should handle empty response`() = runTest {
        val emptyResponse = fakePagedActors(items = emptyList(), totalPages = 0, totalItems = 0)
        coEvery { repository.getTrendingActors(any()) } returns emptyResponse

        val result = useCase.invoke(page = 1)
        assertEquals(emptyResponse, result)
    }

    @Test
    fun `invoke should handle multiple actors in response`() = runTest {
        val response = fakePagedActors(
            items = listOf(
                fakeActor(id = 1, name = "Actor 1"),
                fakeActor(id = 2, name = "Actor 2"),
                fakeActor(id = 3, name = "Actor 3")
            )
        )
        coEvery { repository.getTrendingActors(any()) } returns response

        val result = useCase.invoke(page = 1)
        assertEquals(response, result)
    }

    @Test
    fun `invoke should handle repository error`() = runTest {
        coEvery { repository.getTrendingActors(any()) } throws Exception("Repository error")

        val exception = runCatching { useCase.invoke(page = 1) }.exceptionOrNull()
        assertEquals("Repository error", exception?.message)
    }

    @Test
    fun `invoke should handle negative page number`() = runTest {
        val mockResponse = fakePagedActors()
        coEvery { repository.getTrendingActors(any()) } returns mockResponse

        val result = useCase.invoke(page = -1)
        assertEquals(mockResponse.currentPage, result.currentPage)
    }

    @Test
    fun `invoke should handle zero page number`() = runTest {
        val mockResponse = fakePagedActors()
        coEvery { repository.getTrendingActors(any()) } returns mockResponse

        val result = useCase.invoke(page = 0)
        assertEquals(mockResponse.currentPage, result.currentPage)
    }

    @Test
    fun `invoke should handle large page number`() = runTest {
        val mockResponse = fakePagedActors()
        coEvery { repository.getTrendingActors(any()) } returns mockResponse

        val result = useCase.invoke(page = 999)
        assertNotNull(result)
        assertEquals(1, result.currentPage)
    }

    @Test
    fun `invoke should handle actors with different character names`() = runTest {
        val response = fakePagedActors(
            items = listOf(
                fakeActor(id = 1, name = "Actor 1", characterName = "Character 1"),
                fakeActor(id = 2, name = "Actor 2", characterName = "Character 2"),
                fakeActor(id = 3, name = "Actor 3", characterName = "")
            )
        )
        coEvery { repository.getTrendingActors(any()) } returns response

        val result = useCase.invoke(page = 1)
        assertEquals(response.items.map { it.characterName }, result.items.map { it.characterName })
    }

    @Test
    fun `invoke should handle actors with different profile pictures`() = runTest {
        val response = fakePagedActors(
            items = listOf(
                fakeActor(id = 1, name = "Actor 1", profilePicture = "profile1.jpg"),
                fakeActor(id = 2, name = "Actor 2", profilePicture = "profile2.jpg"),
                fakeActor(id = 3, name = "Actor 3", profilePicture = "")
            )
        )
        coEvery { repository.getTrendingActors(any()) } returns response

        val result = useCase.invoke(page = 1)
        assertEquals(
            response.items.map { it.profilePictureUrl },
            result.items.map { it.profilePictureUrl })
    }

    private fun fakePagedActors(
        currentPage: Int = 1,
        items: List<Actor> = listOf(fakeActor()),
        totalPages: Int = 10,
        totalItems: Int = 100
    ): PagedFetchResponse<Actor> = PagedFetchResponse(currentPage, items, totalPages, totalItems)

    private fun fakeActor(
        id: Int = 1,
        name: String = "Test Actor",
        profilePicture: String = "test_profile.jpg",
        characterName: String = ""
    ) = Actor(
        id = id, name = name, profilePictureUrl = profilePicture, characterName = characterName
    )
}
