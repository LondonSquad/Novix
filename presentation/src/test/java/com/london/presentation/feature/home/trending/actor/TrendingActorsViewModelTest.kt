package com.london.presentation.feature.home.trending.actor

import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.usecase.GetTrendingActorsUseCase
import com.london.presentation.shared.base.ErrorState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


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
                listOf(createMockActor())

        )

        // When
        advanceUntilIdle()

        //Then
        viewModel.state.test {
            val actors = expectMostRecentItem().actorsFlow.first()
            assertThat(actors).isInstanceOf(PagingData::class.java)
        }
    }

    @Test
    fun `when retry is called ,should success updates state correctly `() = runTest {
        // Given
        coEvery { getTrendingActors.invoke(1) } returns createMockPagedFetchResponse(
            listOf(createMockActor())
        )

        // When
        advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state.errorState).isNull()
            assertThat(state.isLoading).isFalse()
        }
    }
    @Test
    fun `when onActorClick, should emits NavigateToActor effect`() = runTest {

        // When & Then
        viewModel.effect.test {
            viewModel.onActorClick(1)
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(TrendingActorsEffect.NavigateToActor::class.java)
        }
    }
    
    @Test
    fun `when onBack, should emits NavigateBack effect`() = runTest {
        // When & Then
        viewModel.effect.test {
            viewModel.onBackClick()
            val effect = awaitItem()
            assertThat(effect).isInstanceOf(TrendingActorsEffect.NavigateBack::class.java)
        }
    }
    
    @Test
    fun `when click retry, should reload trending actors`() = runTest {
        // When & Then
        viewModel.state.test {
            viewModel.onRetryClick()
            val state = expectMostRecentItem()
            assertThat(state.errorState).isNull()
            assertThat(state.isLoading).isFalse()
        }
    }

    @Test
    fun `when fetching trending actors and an error occurs,should update error state and loading state correctly`() = runTest {
       viewModel.handlingErrorState(ErrorState.NoInternet)
       
            // When & Then
            viewModel.state.test {
                viewModel.onRetryClick()
                val state = expectMostRecentItem()
                assertThat(state.errorState).isNotNull()
                assertThat(state.isLoading).isFalse()
                cancelAndIgnoreRemainingEvents()
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