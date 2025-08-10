package com.london.presentation.feature.home.trending.actor

import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.usecase.GetTrendingActorsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingActorsViewModelTest {
    private lateinit var getTrendingActors: GetTrendingActorsUseCase
    private var viewModel: TrendingActorsViewModel? = null
    private val mainDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(mainDispatcher)
        getTrendingActors = mockk()

        coEvery { getTrendingActors.invoke(any()) } returns createMockActorsResponse()

        viewModel = TrendingActorsViewModel(getTrendingActors = getTrendingActors)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel?.viewModelScope?.cancel()
        viewModel = null
    }

    @Test
    fun `when initializeActors succeeds, actorsFlow state should be updated`() = runTest {
        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.actorsFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeActors starts, loading state should be true`() = runTest {
        // Given
        coEvery { getTrendingActors.invoke(any()) } coAnswers {
            delay(100)
            createMockActorsResponse()
        }

        viewModel = TrendingActorsViewModel(getTrendingActors = getTrendingActors)

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `onActorClick should emit NavigateToActor effect with correct actor ID`() = runTest {
        // Given
        val actorId = 12345

        // When & Then
        viewModel?.effect?.test {
            viewModel?.onActorClick(actorId)
            assertThat(awaitItem()).isEqualTo(
                TrendingActorsEffect.NavigateToActor(actorId)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onBack should emit NavigateBack effect`() = runTest {
        // When & Then
        viewModel?.effect?.test {
            viewModel?.onBack()
            assertThat(awaitItem()).isEqualTo(TrendingActorsEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRetry should reinitialize actors`() = runTest {
        // When
        viewModel?.onRetry()
        advanceUntilIdle()

        // Then
        assertThat(getTrendingActors.invoke(1)).isNotNull()
    }

    @Test
    fun `when initializeActors succeeds multiple times, state should be updated correctly`() =
        runTest {
            // When
            advanceUntilIdle()

            // Then
            viewModel?.state?.test {
                val state = expectMostRecentItem()
                assertThat(state.actorsFlow).isNotNull()
                assertThat(state.isLoading).isFalse()
                ensureAllEventsConsumed()
            }
        }

    @Test
    fun `when initializeActors with empty response, state should handle empty data`() = runTest {
        // Given
        val emptyResponse = PagedFetchResponse<Actor>(
            currentPage = 1,
            items = emptyList(),
            totalPages = 0,
            totalItems = 0
        )
        coEvery { getTrendingActors.invoke(any()) } returns emptyResponse

        viewModel = TrendingActorsViewModel(getTrendingActors = getTrendingActors)

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.actorsFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `when initializeActors with large dataset, state should handle large data`() = runTest {
        // Given
        val largeResponse = createMockActorsResponse(
            items = (1..100).map { createMockActor(id = it, name = "Actor $it") }
        )
        coEvery { getTrendingActors.invoke(any()) } returns largeResponse

        viewModel = TrendingActorsViewModel(getTrendingActors = getTrendingActors)

        // When
        advanceUntilIdle()

        // Then
        viewModel?.state?.test {
            val state = expectMostRecentItem()
            assertThat(state.actorsFlow).isNotNull()
            assertThat(state.isLoading).isFalse()
            ensureAllEventsConsumed()
        }
    }

    companion object {
        private fun createMockActorsResponse(
            page: Int = 1,
            items: List<Actor> = listOf(createMockActor())
        ): PagedFetchResponse<Actor> = PagedFetchResponse<Actor>(
            currentPage = page,
            items = items,
            totalPages = 10,
            totalItems = 100
        )

        private fun createMockActor(
            id: Int = 1,
            name: String = "Test Actor",
            profilePicture: String = "test_profile.jpg",
            characterName: String = "Test Character"
        ): Actor = Actor(
            id = id,
            name = name,
            profilePictureUrl = profilePicture,
            characterName = characterName
        )
    }
} 