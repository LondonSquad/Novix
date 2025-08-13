package com.london.presentation.feature.home.trending.actor

import app.cash.turbine.test
import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.usecase.GetTrendingActorsUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingActorsViewModelTest {
    private lateinit var viewModel: TrendingActorsViewModel
    private val getTrendingActors: GetTrendingActorsUseCase = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { getTrendingActors.invoke(any()) } returns createMockPagedFetchResponse(emptyList())
        viewModel = createViewModel()
    }

    private fun createViewModel() = TrendingActorsViewModel(getTrendingActors)

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `when initializing actorData , should fetch trending actors`() = runTest {
        //Given 
        coEvery { getTrendingActors.invoke(any()) } returns createMockPagedFetchResponse(
            listOf(
                createMockActor()
            )
        )

        // When
        advanceUntilIdle()

        //Then
        viewModel.state.test {
            assertEquals(createMockActor(), )
        }
    }

    private fun createMockActor() = mockk<Actor> {
        every { id } returns 1
        every { name } returns "Actor Name"
        every { profilePictureUrl } returns "https://example.com/profile.jpg"
        every { characterName } returns "Character Name"
    }

    private fun <T> createMockPagedFetchResponse(data: List<T>) = mockk<PagedFetchResponse<T>> {
        every { currentPage } returns 1
        every { items } returns data
        every { totalPages } returns 1
        every { totalItems } returns data.size
    }
}